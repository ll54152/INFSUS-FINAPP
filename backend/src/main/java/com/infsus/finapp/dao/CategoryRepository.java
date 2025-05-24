package com.infsus.finapp.dao;

import com.infsus.finapp.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);

    int countByCategoryName(String categoryName);

    List<Category> deleteById(long id);

    List<Category> findAllByPerson_PersonId(Long personId);

    Category findById(long categoryId);
}

