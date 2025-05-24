package com.infsus.finapp.service;

import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<Category> listAll();

    int countByCategoryName(String categoryName);

    Category findByCategoryName(String categoryName);

    List<Category> listPersonsCategories(String email);

    Category findByCategoryId(long categoryId);

    String createCategory(CategoryDTO categoryDTO, Person person);

    void deleteCategory(long id);
}
