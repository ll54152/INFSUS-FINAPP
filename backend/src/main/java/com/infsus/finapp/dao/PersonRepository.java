package com.infsus.finapp.dao;

import com.infsus.finapp.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    int countByEmail(String email);

    Person findByEmail(String email);

    Person findByPersonId(Long personId);
}
