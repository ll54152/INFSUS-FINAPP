package com.infsus.finapp.service;

import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.PersonDTO;

public interface PersonService {
    long createPerson(PersonDTO personDTO);

    Person findByEmail(String email);

    Person findByPersonId(Long personId);

    Integer countByEmail(String email);
}
