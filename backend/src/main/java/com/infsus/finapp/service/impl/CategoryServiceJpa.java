package com.infsus.finapp.service.impl;

import com.infsus.finapp.dao.CategoryRepository;
import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.service.CategoryService;
import com.infsus.finapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CategoryServiceJpa implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    PersonService personService;

    @Override
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    @Override
    public int countByCategoryName(String categoryName) {
        return categoryRepository.countByCategoryName(categoryName);
    }

    @Override
    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Category> listPersonsCategories(String email) {
        Person person = personService.findByEmail(email);
        return categoryRepository.findAllByPerson_PersonId(person.getPersonId());
    }

    @Override
    public Category findByCategoryId(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public String createCategory(CategoryDTO categoryDTO, Person person) {
        Assert.hasText(categoryDTO.getCategoryName(), "Ime kategorije mora biti navedeno!");
        if (person.getCategories().contains(categoryRepository.findById(categoryDTO.getId()))) {
            Assert.hasText("", "Kategorija s tim nazivom već postoji!");
        }
        Category category = new Category(
                categoryDTO.getId(),
                categoryDTO.getCategoryName(),
                person
        );
        categoryRepository.save(category);
        return category.getCategoryName();
    }

    @Override
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            //Assert.hasText("", "Kategorija s nazivom " + category.getCategoryName() + " ne postoji!");
            throw new IllegalArgumentException("Kategorija s tim ID-em ne postoji!");
        }

        categoryRepository.deleteById(id);
    }
}

