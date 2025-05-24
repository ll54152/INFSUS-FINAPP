package com.infsus.finapp.dao;

import com.infsus.finapp.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);

    int countByAccountName(String accountName);

    void deleteById(long id);

    List<Account> findAllByPerson_PersonId(Long personId);

    Account findById(long accountId);
}
