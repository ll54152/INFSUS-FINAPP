package com.infsus.finapp.service.impl;

import com.infsus.finapp.dao.AccountRepository;
import com.infsus.finapp.dao.TransactionRepository;
import com.infsus.finapp.domain.Account;
import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.domain.Transaction;
import com.infsus.finapp.domain.Currency;
import com.infsus.finapp.dto.CategoryTransactionSummaryDTO;
import com.infsus.finapp.dto.DailyTransactionSummaryDTO;
import com.infsus.finapp.dto.TransactionDTO;
import com.infsus.finapp.service.AccountService;
import com.infsus.finapp.service.TransactionService;
import com.infsus.finapp.service.CategoryService;
import com.infsus.finapp.service.CurrencyService;
import com.infsus.finapp.service.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionServiceJpa implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PersonService personService;

    @Override
    public List<Transaction> listAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> listPersonsTransactions(String email) {
        Person person = personService.findByEmail(email);
        return transactionRepository.findAllByPerson_PersonId(person.getPersonId());
    }

    @Override
    public List<Transaction> listAccountsTransactions(long id) {
        Account account = accountService.findByAccountId(id);
        return transactionRepository.findAllByAccount_Id(id);
    }

    @Override
    public List<TransactionDTO> listCategoryTransactions(long id) {

        Category category = categoryService.findByCategoryId(id);
        List<Transaction> transactions = transactionRepository.findAllByCategory_Id(id);
        List<TransactionDTO> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals("rashod") && transaction.isDone()) {
                TransactionDTO transactionDTO = new TransactionDTO(
                        transaction.getId(),
                        transaction.getTransactionName(),
                        transaction.getTransactionAmount(),
                        transaction.getDateOfTransaction(),
                        transaction.getTransactionType(),
                        transaction.getNote(),
                        transaction.getCategory().getCategoryName(),
                        transaction.getAccount().getAccountName(),
                        transaction.getCurrency().getCurrencyCode()
                );
                filteredTransactions.add(transactionDTO);
            }
        }
        return filteredTransactions;
    }

    @Override
    public int countByTransactionName(String transactionName) {
        return transactionRepository.countByTransactionName(transactionName);
    }

    @Override
    public Transaction findById(long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction findByTransactionName(String transactionName) {
        return transactionRepository.findByTransactionName(transactionName);
    }

    @Override
    public void createTransaction(TransactionDTO transactionDTO, Person person, Account accountTo) {

        Assert.hasText(transactionDTO.getTransactionName(), "Ime transakcije mora biti navedeno!");
        if (person.getTransactions().contains(transactionRepository.findByTransactionName(transactionDTO.getTransactionName()))) {
            Assert.hasText("", "Transakcija s tim nazivom već postoji!");
        }
        Assert.notNull(transactionDTO.getTransactionAmount(), "Iznos transakcije mora biti naveden!");
        Assert.notNull(transactionDTO.getDateOfTransaction(), "Datum transakcije mora biti naveden!");
        Assert.notNull(transactionDTO.getTypeOfTransaction(), "Tip transakcije mora biti naveden!");
        Currency currency = currencyService.findByCurrencyCode(transactionDTO.getCurrency());
        Assert.notNull(currency, "Morate odabrati valutu!");

        Set<Category> categories = person.getCategories();
        Category category1 = null;
        for (Category category : categories) {
            if (category.getCategoryName().equals(transactionDTO.getCategory())) {
                category1 = category;
            }
        }
        Assert.notNull(category1, "Morate odabrati kategoriju!");

        Set<Account> accounts = person.getAccounts();

        Account account1 = null;
        for (Account account : accounts) {
            if (account.getAccountName().equals(transactionDTO.getAccountName())) {
                account1 = account;
            }
        }
        Assert.notNull(account1, "Morate odabrati račun!");
        if (accountTo == null) {

            Transaction transaction = new Transaction(
                    transactionDTO.getId(),
                    transactionDTO.getDateOfTransaction(),
                    transactionDTO.getTransactionName(),
                    transactionDTO.getTransactionAmount(),
                    transactionDTO.getTypeOfTransaction(),
                    transactionDTO.getNote(),
                    false,
                    person,
                    currency,
                    category1,
                    account1
            );
            transactionRepository.save(transaction);
        } else {
            Transaction transaction1 = new Transaction(
                    transactionDTO.getId(),
                    transactionDTO.getDateOfTransaction(),
                    transactionDTO.getTransactionName(),
                    transactionDTO.getTransactionAmount(),
                    "rashod",
                    transactionDTO.getNote(),
                    false,
                    person,
                    currency,
                    category1,
                    account1
            );
            Transaction transaction2 = new Transaction(
                    transactionDTO.getId(),
                    transactionDTO.getDateOfTransaction(),
                    transactionDTO.getTransactionName() + " (uplata)",
                    transactionDTO.getTransactionAmount(),
                    "prihod",
                    transactionDTO.getNote(),
                    false,
                    person,
                    currency,
                    category1,
                    accountTo
            );
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
        }
    }

    @Override
    @Transactional
    public void updateTransaction(long id, TransactionDTO transactionDTO, Person person) {
        Transaction existingTransaction = transactionRepository.findById(id);
        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transakcija s nazivom " + transactionDTO.getTransactionName() + " ne postoji!");
        }

        Assert.hasText(transactionDTO.getTransactionName(), "Ime transakcije mora biti navedeno!");
        Assert.notNull(transactionDTO.getTransactionAmount(), "Iznos transakcije mora biti naveden!");
        Assert.notNull(transactionDTO.getDateOfTransaction(), "Datum transakcije mora biti naveden!");
        Assert.notNull(transactionDTO.getTypeOfTransaction(), "Tip transakcije mora biti naveden!");
        Currency currency = currencyService.findByCurrencyCode(transactionDTO.getCurrency());
        Assert.notNull(currency, "Morate odabrati valutu!");

        Set<Category> categories = person.getCategories();
        Category category1 = null;
        for (Category category : categories) {
            if (category.getCategoryName().equals(transactionDTO.getCategory())) {
                category1 = category;
            }
        }
        Assert.notNull(category1, "Morate odabrati kategoriju!");
        Set<Account> accounts = person.getAccounts();

        Account account1 = null;
        for (Account account : accounts) {
            if (account.getAccountName().equals(transactionDTO.getAccountName())) {
                account1 = account;
            }
        }
        Assert.notNull(account1, "Morate odabrati račun!");

        if (existingTransaction.getTransactionType().equals("prihod")) {

            double amount = (existingTransaction.getTransactionAmount() * existingTransaction.getCurrency().getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() - amount);

        } else if (existingTransaction.getTransactionType().equals("rashod")) {

            double amount = (existingTransaction.getTransactionAmount() * existingTransaction.getCurrency().getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() + amount);
        }

        if (transactionDTO.getTypeOfTransaction().equals("prihod")) {
            double amount2 = (transactionDTO.getTransactionAmount() * currency.getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() + amount2);
        } else if (transactionDTO.getTypeOfTransaction().equals("rashod")) {
            double amount2 = (transactionDTO.getTransactionAmount() * currency.getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() - amount2);
        }

        accountRepository.save(account1);

        existingTransaction.setTransactionName(transactionDTO.getTransactionName());
        existingTransaction.setTransactionAmount(transactionDTO.getTransactionAmount());
        existingTransaction.setDateOfTransaction(transactionDTO.getDateOfTransaction());
        existingTransaction.setTransactionType(transactionDTO.getTypeOfTransaction());
        existingTransaction.setNote(transactionDTO.getNote());
        existingTransaction.setCurrency(currency);
        existingTransaction.setCategory(category1);
        existingTransaction.setAccount(account1);

        transactionRepository.save(existingTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(long id, Person person) {
        Transaction transaction = transactionRepository.findById(id);

        if (transaction == null) {
            //Assert.hasText("", "Račun s nazivom " + transaction.getTransactionName() + " ne postoji!");
            throw new IllegalArgumentException("Transakcija s tim ID-em ne postoji!");
        }

        Set<Account> accounts = person.getAccounts();

        Account account1 = new Account();
        for (Account account : accounts) {
            if (account.getAccountName().equals(transaction.getAccount().getAccountName())) {
                account1 = account;
            }
        }

        if (transaction.getTransactionType().equals("prihod")) {
            double amount = (transaction.getTransactionAmount() * transaction.getCurrency().getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() - amount);
        } else if (transaction.getTransactionType().equals("rashod")) {
            double amount = (transaction.getTransactionAmount() * transaction.getCurrency().getConversionToEuro()) / account1.getCurrency().getConversionToEuro();
            account1.setBalance(account1.getBalance() + amount);
        }

        accountRepository.save(account1);
        transactionRepository.deleteById(id);
    }

    @Override
    public List<DailyTransactionSummaryDTO> listTransactionsByDateAndAccount(String email, Date startDate, Date endDate, String accountName) {
        Person person = personService.findByEmail(email);
        List<Transaction> personsTransactions = listPersonsTransactions(person.getEmail());

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : personsTransactions) {
            if (transaction.getAccount().getAccountName().equals(accountName) && transaction.isDone()) {
                filteredTransactions.add(transaction);
            }
        }

        Map<Date, DailyTransactionSummaryDTO> dailySummaryMap = new HashMap<>();

        for (Transaction transaction : filteredTransactions) {
            Date transactionDate = transaction.getDateOfTransaction();
            dailySummaryMap.putIfAbsent(transactionDate, new DailyTransactionSummaryDTO(transactionDate, 0, 0));

            DailyTransactionSummaryDTO summary = dailySummaryMap.get(transactionDate);
            if (transaction.getTransactionType().equalsIgnoreCase("prihod")) {
                summary.setIncomeCount(summary.getIncomeCount() + transaction.getTransactionAmount());
            } else if (transaction.getTransactionType().equalsIgnoreCase("rashod")) {
                summary.setExpenseCount(summary.getExpenseCount() - transaction.getTransactionAmount());
            }
        }

        return new ArrayList<>(dailySummaryMap.values());
    }

    @Override
    public List<Transaction> listTransactionsByDateAndAccountPDF(String email, Date startDate, Date endDate, String accountName) {
        Person person = personService.findByEmail(email);
        List<Transaction> personsTransactions = listPersonsTransactions(person.getEmail());

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : personsTransactions) {
            if (transaction.getAccount().getAccountName().equals(accountName) && transaction.isDone()) {
                filteredTransactions.add(transaction);
            }
        }

        List<Transaction> filteredTransactions2 = new ArrayList<>();

        for (Transaction transaction : filteredTransactions) {
            if (!transaction.getDateOfTransaction().before(startDate) && !transaction.getDateOfTransaction().after(endDate)) {
                filteredTransactions2.add(transaction);
            }
        }
        return filteredTransactions2;
    }

    @Override
    public List<CategoryTransactionSummaryDTO> listTransactionsByDateAndAccountForPie(String email, Date startDate, Date endDate, String accountName) {
        Person person = personService.findByEmail(email);
        List<Transaction> personsTransactions = listPersonsTransactions(person.getEmail());

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : personsTransactions) {
            if (transaction.getAccount().getAccountName().equals(accountName) && transaction.isDone()) {
                filteredTransactions.add(transaction);
            }
        }

        Map<String, CategoryTransactionSummaryDTO> categorySummaryMap = new HashMap<>();

        for (Transaction transaction : filteredTransactions) {
            String categoryName = transaction.getCategory().getCategoryName();
            categorySummaryMap.putIfAbsent(categoryName, new CategoryTransactionSummaryDTO(categoryName, 0, 0));

            CategoryTransactionSummaryDTO summary = categorySummaryMap.get(categoryName);
            if (transaction.getTransactionType().equalsIgnoreCase("prihod")) {
                summary.setIncomeAmount(summary.getIncomeAmount() + transaction.getTransactionAmount());
            } else if (transaction.getTransactionType().equalsIgnoreCase("rashod")) {
                summary.setExpenseAmount(summary.getExpenseAmount() - transaction.getTransactionAmount());
            }
        }
        return new ArrayList<>(categorySummaryMap.values());
    }

}
