package com.infsus.finapp.rest;

import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.PersonDTO;
import com.infsus.finapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.service.PersonService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/register")
    public long createPerson(@RequestBody PersonDTO personDTO) {
        long id = personService.createPerson(personDTO);
        Person person = personService.findByPersonId(id);
        categoryService.createCategory(new CategoryDTO(1, "Plaća"), person);
        categoryService.createCategory(new CategoryDTO(2, "Hrana/Piće"), person);
        categoryService.createCategory(new CategoryDTO(3, "Kuća"), person);
        categoryService.createCategory(new CategoryDTO(4, "Stanarina"), person);
        categoryService.createCategory(new CategoryDTO(5, "Trgovina"), person);
        categoryService.createCategory(new CategoryDTO(6, "Odjeća"), person);
        categoryService.createCategory(new CategoryDTO(7, "Mirovina"), person);
        categoryService.createCategory(new CategoryDTO(8, "Putovanje"), person);
        return id;
    }


}

