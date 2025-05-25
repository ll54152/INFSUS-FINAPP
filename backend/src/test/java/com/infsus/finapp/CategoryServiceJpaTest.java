package com.infsus.finapp;

import com.infsus.finapp.dao.CategoryRepository;
import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.impl.CategoryServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceJpaTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private CategoryServiceJpa categoryService;

    private Person person;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setPersonId(1L);

        category = new Category();
        category.setId(1L);
        category.setCategoryName("TestCategory");
        category.setPerson(person);

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setCategoryName("TestCategory");
    }

    @Test
    void listAll_shouldReturnAllCategories() {
        given(categoryRepository.findAll()).willReturn(List.of(category));

        List<Category> categories = categoryService.listAll();

        assertEquals(1, categories.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void countByCategoryName_shouldReturnCount() {
        given(categoryRepository.countByCategoryName("TestCategory")).willReturn(3);

        int count = categoryService.countByCategoryName("TestCategory");

        assertEquals(3, count);
    }

    @Test
    void findByCategoryName_shouldReturnCategory() {
        given(categoryRepository.findByCategoryName("TestCategory")).willReturn(category);

        Category result = categoryService.findByCategoryName("TestCategory");

        assertEquals(category, result);
    }

    @Test
    void listPersonsCategories_shouldReturnCategoriesForPerson() {
        given(personService.findByEmail("email@test.com")).willReturn(person);
        given(categoryRepository.findAllByPerson_PersonId(person.getPersonId())).willReturn(List.of(category));

        List<Category> categories = categoryService.listPersonsCategories("email@test.com");

        assertEquals(1, categories.size());
        verify(personService).findByEmail("email@test.com");
        verify(categoryRepository).findAllByPerson_PersonId(person.getPersonId());
    }

    @Test
    void findByCategoryId_shouldReturnCategory() {
        given(categoryRepository.findById(1L)).willReturn(category);

        Category result = categoryService.findByCategoryId(1L);

        assertEquals(category, result);
    }

    @Test
    void createCategory_shouldSaveAndReturnCategoryName() {
        given(categoryRepository.findById(categoryDTO.getId())).willReturn(null);

        String categoryName = categoryService.createCategory(categoryDTO, person);

        assertEquals("TestCategory", categoryName);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_duplicateCategory_shouldThrow() {
        given(categoryRepository.findById(categoryDTO.getId())).willReturn(category);
        person.getCategories().add(category);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(categoryDTO, person);
        });

        assertTrue(exception.getMessage().contains("Kategorija s tim nazivom već postoji"));
    }

    @Test
    void createCategory_emptyName_shouldThrow() {
        categoryDTO.setCategoryName("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(categoryDTO, person);
        });

        assertTrue(exception.getMessage().contains("Ime kategorije mora biti navedeno"));
    }

    @Test
    void deleteCategory_existingCategory_shouldDelete() {
        given(categoryRepository.findById(1L)).willReturn(category);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_nonExistingCategory_shouldThrow() {
        given(categoryRepository.findById(1L)).willReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertTrue(exception.getMessage().contains("ne postoji"));
    }
}