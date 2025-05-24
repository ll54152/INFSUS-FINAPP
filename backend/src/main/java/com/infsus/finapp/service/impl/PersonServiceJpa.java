package com.infsus.finapp.service.impl;


import com.infsus.finapp.dao.PersonRepository;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.PersonDTO;
import com.infsus.finapp.service.PersonService;
import com.infsus.finapp.service.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class PersonServiceJpa implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    private static final String LOZINKA_FORMAT = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    @Override
    public long createPerson(PersonDTO personDTO) {
        Assert.notNull(personDTO, "Podaci o korisniku moraju biti navedeni.");
        Assert.isNull(personDTO.getId(),
                "ID korisnika mora imati vrijednost null, a ne " + personDTO.getId() + "."
        );
        String email = personDTO.getEmail();
        Assert.hasText(email, "Email mora biti naveden.");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email mora biti u ispravnom obliku, npr. user@example.com, a ne '" + email + "'."
        );
        String ime = personDTO.getName();
        Assert.hasText(ime, "Ime mora biti navedeno.");
        String prezime = personDTO.getSurname();
        Assert.hasText(prezime, "Prezime mora biti navedeno.");

        String lozinka = personDTO.getPassword();
        Assert.hasText(lozinka, "Lozinka mora biti navedena.");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Lozinka mora biti u pravilnom obliku - barem jedan broj, jedno veliko slovo, jedno malo slovo " +
                        "i mora sadržavati barem osam znakova."
        );
        Person person = new Person(
                personDTO.getId(),
                personDTO.getEmail(),
                this.passwordEncoder.encode(personDTO.getPassword()),
                personDTO.getName(),
                personDTO.getSurname()
        );
        if (personRepository.countByEmail((person.getEmail())) > 0) {
            Assert.hasText("", "Korisnik s tim emailom već postoji!");
            throw new RequestDeniedException(
                    "Korisnik s tim email-om već postoji!"
            );
        }
        personRepository.save(person);
        return person.getPersonId();
    }

    @Override
    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public Person findByPersonId(Long personId) {
        return personRepository.findByPersonId(personId);
    }

    @Override
    public Integer countByEmail(String email) {
        return personRepository.countByEmail(email);
    }
}
