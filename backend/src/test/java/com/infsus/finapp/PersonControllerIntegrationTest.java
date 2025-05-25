package com.infsus.finapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.PersonDTO;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.rest.PersonController;
import com.infsus.finapp.service.CategoryService;
import com.infsus.finapp.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PersonController.class)
public class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testCreatePerson() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Test User");
        personDTO.setEmail("testuser@example.com");
        personDTO.setPassword("password123");

        long generatedId = 42L;
        Person person = new Person();
        person.setPersonId(generatedId);

        Mockito.when(personService.createPerson(Mockito.any(PersonDTO.class))).thenReturn(generatedId);
        Mockito.when(personService.findByPersonId(generatedId)).thenReturn(person);

        mockMvc.perform(post("/api/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(generatedId)));

        ArgumentCaptor<CategoryDTO> categoryCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
        Mockito.verify(categoryService, Mockito.times(8)).createCategory(categoryCaptor.capture(), Mockito.eq(person));

        assertThat(categoryCaptor.getAllValues())
                .extracting(CategoryDTO::getCategoryName)
                .containsExactlyInAnyOrder("Salary", "Food/Drinks", "House", "Rent", "Groceries", "Clothes", "Pension", "Travel");
    }
}