package com.infsus.finapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.dto.PersonDTO;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.rest.PersonController;
import com.infsus.finapp.service.CategoryService;
import com.infsus.finapp.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPerson_shouldReturnIdAndCallServices() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEmail("test@example.com");
        personDTO.setPassword("password");
        personDTO.setName("Test User");

        long generatedId = 123L;
        Person mockPerson = new Person();
        mockPerson.setPersonId(generatedId);

        when(personService.createPerson(any(PersonDTO.class))).thenReturn(generatedId);
        when(personService.findByPersonId(generatedId)).thenReturn(mockPerson);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(generatedId)));

        verify(personService).createPerson(any(PersonDTO.class));
        verify(personService).findByPersonId(generatedId);
        verify(categoryService, times(8)).createCategory(any(CategoryDTO.class), eq(mockPerson));
    }
}