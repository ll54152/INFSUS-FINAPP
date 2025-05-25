package com.infsus.finapp;

import com.infsus.finapp.dao.PersonRepository;
import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.rest.CategoryController;
import com.infsus.finapp.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Person mockPerson;
    private User mockUser;

    @BeforeEach
    void setup() {
        mockPerson = new Person();
        mockPerson.setEmail("testuser@example.com");

        mockUser = new User("testuser@example.com", "password", List.of());
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testCreateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("TestCategory");

        when(personRepository.findByEmail("testuser@example.com")).thenReturn(mockPerson);

        mockMvc.perform(post("/category/createCategory")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryDTO))
                        .with(csrf()))
                .andExpect(status().isOk());

        ArgumentCaptor<CategoryDTO> captorDto = ArgumentCaptor.forClass(CategoryDTO.class);
        ArgumentCaptor<Person> captorPerson = ArgumentCaptor.forClass(Person.class);

        verify(categoryService, times(1)).createCategory(captorDto.capture(), captorPerson.capture());

        assertThat(captorDto.getValue().getCategoryName()).isEqualTo("TestCategory");
        assertThat(captorPerson.getValue().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testListCategories() throws Exception {
        Category cat1 = new Category(1L, "Food");
        Category cat2 = new Category(2L, "Travel");
        List<Category> categories = Arrays.asList(cat1, cat2);

        when(categoryService.listPersonsCategories("testuser@example.com")).thenReturn(categories);

        mockMvc.perform(get("/category/listCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Food"))
                .andExpect(jsonPath("$[1]").value("Travel"));
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testListCategoryDetails() throws Exception {
        Category cat1 = new Category(1L, "Food");
        Category cat2 = new Category(2L, "Travel");
        List<Category> categories = Arrays.asList(cat1, cat2);

        when(categoryService.listPersonsCategories("testuser@example.com")).thenReturn(categories);

        mockMvc.perform(get("/category/listCategoryDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Food"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("Travel"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        long idToDelete = 5L;

        mockMvc.perform(get("/category/deleteCategory/5")
                        .with(user("testuser@example.com").roles("USER")))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(idToDelete);
    }
}