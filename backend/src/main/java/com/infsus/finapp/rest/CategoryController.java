package com.infsus.finapp.rest;

import com.infsus.finapp.dao.PersonRepository;
import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    PersonRepository personService;


    @PostMapping(path = "/createCategory")
    public void createCategory(@RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        Person person = personService.findByEmail(email);
        categoryService.createCategory(categoryDTO, person);
    }

    @GetMapping("/listCategories")
    public List<String> listCategories(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Category> categories = categoryService.listPersonsCategories(email);
        List<String> show = new ArrayList<>();

        for (Category category : categories) {
            show.add(category.getCategoryName());
        }
        return show;
    }

    @GetMapping("/listCategoryDetails")
    public List<Category> listPersonsCategories(@AuthenticationPrincipal User user) {
        String email = user.getUsername();
        List<Category> categories = categoryService.listPersonsCategories(email);
        return categories.stream()
                .map(category -> new Category(category.getId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/deleteCategory/{id}")
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategory(id);
    }

}
