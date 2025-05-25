-------------------Person---------------------------------------------------------------------------------------------------------------------------
CREATE TABLE Person (
    personId SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL
);
----------------------------------------------------------------------------------------------------------------------------------------------------



-------------------Currency-------------------------------------------------------------------------------------------------------------------------
CREATE TABLE Currency (
    currencyCode VARCHAR(3) PRIMARY KEY,
    currency_name VARCHAR(100) NOT NULL,
    currency_symbol VARCHAR(10),
    conversion_to_euro DOUBLE PRECISION NOT NULL
);
----------------------------------------------------------------------------------------------------------------------------------------------------



-------------------Account--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE Account (
    id SERIAL PRIMARY KEY,
    accountName VARCHAR(100) NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    personId INT,
    currencyCode VARCHAR(3),
    CONSTRAINT fk_person_account FOREIGN KEY (personId) REFERENCES Person(personId),
    CONSTRAINT fk_currency_account FOREIGN KEY (currencyCode) REFERENCES Currency(currencyCode)
);
----------------------------------------------------------------------------------------------------------------------------------------------------



-------------------category_sequence----------------------------------------------------------------------------------------------------------------
CREATE SEQUENCE category_sequence START WITH 9 INCREMENT BY 1;
----------------------------------------------------------------------------------------------------------------------------------------------------



-------------------Category-------------------------------------------------------------------------------------------------------------------------
CREATE TABLE Category (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_sequence'),
    categoryName VARCHAR(100) NOT NULL,
    personId INT,
    CONSTRAINT fk_person_category FOREIGN KEY (personId) REFERENCES Person(personId)
);
----------------------------------------------------------------------------------------------------------------------------------------------------



-------------------Transaction----------------------------------------------------------------------------------------------------------------------
CREATE TABLE Transaction (
    id SERIAL PRIMARY KEY,
    dateOfTransaction TIMESTAMP NOT NULL,
    transactionName VARCHAR(255) NOT NULL,
    transactionAmount DOUBLE PRECISION NOT NULL,
    transactionType VARCHAR(50) NOT NULL,
    note TEXT,
    isDone BOOLEAN NOT NULL,
    personId INT,
    currencyCode VARCHAR(3),
    categoryId BIGINT,
    accountId INT,
    CONSTRAINT fk_person_transaction FOREIGN KEY (personId) REFERENCES Person(personId),
    CONSTRAINT fk_currency_transaction FOREIGN KEY (currencyCode) REFERENCES Currency(currencyCode),
    CONSTRAINT fk_category_transaction FOREIGN KEY (categoryId) REFERENCES Category(id),
    CONSTRAINT fk_account_transaction FOREIGN KEY (accountId) REFERENCES Account(id)
);
----------------------------------------------------------------------------------------------------------------------------------------------------