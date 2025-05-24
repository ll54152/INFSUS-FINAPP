import {Label} from "@radix-ui/react-label";
import {Input} from "../shadcn-components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "../shadcn-components/ui/select";
import AddCategory from "./AddCategory";
import {Button} from "../shadcn-components/ui/button";
import React, {useEffect, useState} from "react";

const AddTransaction = () => {
    const getTodayDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const [error, setError] = useState(null);
    const [transactionName, setTransactionName] = useState('');
    const [dateOfTransaction, setDateOfTransaction] = useState(getTodayDate);
    const [note, setNote] = useState('');
    const [typeOfTransaction, setTypeOfTransaction] = useState(null);
    const [transactionAmount, setTransactionAmount] = useState('');
    const [currency, setCurrency] = useState('');
    const [currencies, setCurrencies] = useState([]);
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState([]);
    const [accountName, setAccountName] = useState('');
    const [accountName2, setAccountName2] = useState(null);
    const [id, setId] = useState('');
    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/currency', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => setCurrencies(data))
            .catch(error => console.error('Error fetching currencies:', error));

        fetch('http://localhost:8080/category/listCategories', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => setCategories(data))
            .catch(error => console.error('Error fetching categories:', error));

        fetch('http://localhost:8080/account/listAccountNames', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => setAccounts(data))
            .catch(error => console.error('Error fetching accounts:', error));

    }, []);

    async function createTransaction(e) {
        e.preventDefault();
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                transactionName: transactionName,
                dateOfTransaction: dateOfTransaction,
                note: note,
                typeOfTransaction: typeOfTransaction,
                transactionAmount: transactionAmount,
                category: category,
                accountName: accountName,
                currency: currency
            }),
        };
        fetch(`http://localhost:8080/transaction/createTransaction/${accountName2}`, options)
            .then((response) => {
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {
                        var newDiv = document.createElement('div');
                        newDiv.className = "border-2 text-white bg-red-400 p-2 rounded-md";
                        newDiv.textContent = data.message;
                        var udiv = document.getElementsByClassName('err')[0];
                        udiv.insertBefore(newDiv, document.getElementById("errMsg"));
                    });
                } else {
                    alert('Transakcija uspješno dodana!');
                    setTransactionName('');
                    setDateOfTransaction(getTodayDate());
                    setNote('');
                    setTypeOfTransaction('');
                    setTransactionAmount('');
                    setCategory('');
                    setAccountName('');
                    setCurrency('');
                    window.location.reload();
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    const updateCategories = (newCategory) => {

        setCategories([...categories, newCategory]);
    };

    return (
        <form onSubmit={createTransaction} className="err">
            <div id="errMsg">
                <Label>Naziv transakcije:</Label>
                <Input className='mb-4'
                       type="text"
                       id="transactionName"
                       value={transactionName}
                       onChange={(e) => setTransactionName(e.target.value)}
                       required
                />
                <Label>Odaberite vrstu transakcije:</Label>
                <Select value={typeOfTransaction}
                        onValueChange={(value) => {
                            setTypeOfTransaction(value);
                        }}>
                    <SelectTrigger className="mb-4">
                        <SelectValue placeholder="Vrsta transakcije"/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="prihod">Prihod</SelectItem>
                        <SelectItem value="rashod">Rashod</SelectItem>
                        <SelectItem value="transakcijeIzmeduRacuna">Transakcija između računa</SelectItem>
                    </SelectContent>
                </Select>
                <Label>Datum transakcije:</Label>
                <Input className='mb-4'
                       type="date"
                       id="dateOfTransaction"
                       value={dateOfTransaction}
                       onChange={(e) => {
                           console.log(e.target.value);
                           setDateOfTransaction(e.target.value)
                       }}
                       required
                />
                <Label>Iznos transakcije:</Label>
                <Input className='mb-4'
                       type="number"
                       id="transactionAmount"
                       value={transactionAmount}
                       onChange={(e) => setTransactionAmount(e.target.value)}
                       required
                />
                <Label>Odaberite valutu:</Label>
                <Select
                    value={currency}
                    onValueChange={(value) => {
                        setCurrency(value);
                    }}
                >
                    <SelectTrigger className="mb-4">
                        <SelectValue placeholder="Valuta"/>
                    </SelectTrigger>
                    <SelectContent>
                        {currencies.map((currency) => (
                            <SelectItem key={currency} value={currency}>
                                {currency}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
                {typeOfTransaction === 'transakcijeIzmeduRacuna' ?
                    <> <Label>Prijenos s računa:</Label>
                        <Select
                            value={accountName}
                            onValueChange={(value) => {
                                setAccountName(value);

                            }}
                        >
                            <SelectTrigger className="mb-4">
                                <SelectValue placeholder="Račun"/>
                            </SelectTrigger>
                            <SelectContent>
                                {accounts.map((account) => (
                                    <SelectItem key={account} value={account}>
                                        {account}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                        <> <Label>Prijenos na račun:</Label>
                            <Select
                                value={accountName2}
                                onValueChange={(value) => {
                                    setAccountName2(value);
                                }}
                            >
                                <SelectTrigger className="mb-4">
                                    <SelectValue placeholder="Račun"/>
                                </SelectTrigger>
                                <SelectContent>
                                    {accounts.map((account) => (
                                        <SelectItem key={account} value={account}>
                                            {account}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select></>
                    </> :
                    <> <Label>Odaberite račun:</Label>
                        <Select
                            value={accountName}
                            onValueChange={(value) => {
                                setAccountName(value);
                            }}
                        >
                            <SelectTrigger className="mb-4">
                                <SelectValue placeholder="Račun"/>
                            </SelectTrigger>
                            <SelectContent>
                                {accounts.map((account) => (
                                    <SelectItem key={account} value={account}>
                                        {account}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select></>
                }

                <Label>Odaberite kategoriju:</Label>
                <Select
                    value={category}
                    onValueChange={(value) => {
                        setCategory(value);
                    }}>
                    <SelectTrigger className="mb-4">
                        <SelectValue placeholder="Kategorija"/>
                    </SelectTrigger>
                    <SelectContent>
                        {categories.map((category, index) => (
                            <SelectItem key={category} value={category}>
                                {category}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
                <div>
                    <AddCategory updateCategories={updateCategories}></AddCategory>
                </div>
                <div>
                    <Label>Bilješka: </Label>
                    <Input className='mb-4'
                           type="text"
                           id="note"
                           value={note}
                           onChange={(e) => setNote(e.target.value)}
                    />
                </div>
            </div>
            <div>
                <Button name="dodaj" type="submit" className='my-4'>Dodaj</Button>
            </div>
        </form>

    );
}

export default AddTransaction;