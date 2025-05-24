package com.infsus.finapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(generator = "category_id_generator")
    @GenericGenerator(
            name = "category_id_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "category_sequence"),
                    @Parameter(name = "initial_value", value = "9"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;
    @NotNull
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    public Category() {
    }

    public Category(long id, String categoryName, Person person) {
        this.id = id;
        this.categoryName = categoryName;
        this.person = person;
    }

    public Category(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

