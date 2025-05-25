package com.infsus.finapp;

import com.infsus.finapp.dao.PersonRepository;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.PersonDTO;
import com.infsus.finapp.service.RequestDeniedException;
import com.infsus.finapp.service.impl.PersonServiceJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceJpaTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonServiceJpa personService;

    private PersonDTO validPersonDTO;

    @BeforeEach
    void setUp() {
        validPersonDTO = new PersonDTO();
        validPersonDTO.setId(null);
        validPersonDTO.setEmail("user@example.com");
        validPersonDTO.setName("John");
        validPersonDTO.setSurname("Doe");
        validPersonDTO.setPassword("Password1");
    }

    @Test
    void createPerson_validData_shouldSaveAndReturnId() {
        given(personRepository.countByEmail("user@example.com")).willReturn(0);
        given(passwordEncoder.encode("Password1")).willReturn("encodedPassword");
        given(personRepository.save(any(Person.class))).willAnswer(invocation -> {
            Person p = invocation.getArgument(0);
            p.setPersonId(42L);
            return p;
        });

        long id = personService.createPerson(validPersonDTO);

        assertEquals(42L, id);
        verify(passwordEncoder).encode("Password1");
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void createPerson_nullDTO_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(null);
        });
        assertTrue(ex.getMessage().contains("moraju biti navedeni"));
    }

    @Test
    void createPerson_idNotNull_shouldThrow() {
        validPersonDTO.setId(10L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertTrue(ex.getMessage().contains("mora imati vrijednost null"));
    }

    @Test
    void createPerson_invalidEmail_shouldThrow() {
        validPersonDTO.setEmail("invalid-email");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertTrue(ex.getMessage().contains("Email mora biti u ispravnom obliku"));
    }

    @Test
    void createPerson_emptyName_shouldThrow() {
        validPersonDTO.setName("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertTrue(ex.getMessage().contains("Ime mora biti navedeno"));
    }

    @Test
    void createPerson_emptySurname_shouldThrow() {
        validPersonDTO.setSurname("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertTrue(ex.getMessage().contains("Prezime mora biti navedeno"));
    }

    @Test
    void createPerson_invalidPassword_shouldThrow() {
        validPersonDTO.setPassword("nopass");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertTrue(ex.getMessage().contains("Lozinka mora biti u pravilnom obliku"));
    }

    @Test
    void createPerson_duplicateEmail_shouldThrowRequestDeniedException() {
        given(personRepository.countByEmail("user@example.com")).willReturn(1);

        RequestDeniedException ex = assertThrows(RequestDeniedException.class, () -> {
            personService.createPerson(validPersonDTO);
        });

        assertEquals("Korisnik s tim email-om već postoji!", ex.getMessage());
    }

    @Test
    void findByEmail_shouldReturnPerson() {
        Person person = new Person();
        person.setPersonId(1L);
        given(personRepository.findByEmail("user@example.com")).willReturn(person);

        Person result = personService.findByEmail("user@example.com");

        assertEquals(person, result);
    }

    @Test
    void findByPersonId_shouldReturnPerson() {
        Person person = new Person();
        person.setPersonId(1L);
        given(personRepository.findByPersonId(1L)).willReturn(person);

        Person result = personService.findByPersonId(1L);

        assertEquals(person, result);
    }

    @Test
    void countByEmail_shouldReturnCount() {
        given(personRepository.countByEmail("user@example.com")).willReturn(2);

        int count = personService.countByEmail("user@example.com");

        assertEquals(2, count);
    }
}