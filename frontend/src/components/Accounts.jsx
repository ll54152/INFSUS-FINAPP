import React, {useEffect, useState} from 'react';
import Header from "./Header";
import {Link} from 'react-router-dom';
import {Label} from "@radix-ui/react-label";
import {Dialog, DialogTitle, DialogTrigger} from "@radix-ui/react-dialog";
import {MdOutlineAddBox} from "react-icons/md";
import {DialogContent} from "../shadcn-components/ui/dialog";
import {Input} from "../shadcn-components/ui/input";
import {Button} from "../shadcn-components/ui/button";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue
} from "../shadcn-components/ui/select";
import {
    Table,
    TableHeader,
    TableBody,
    TableFooter,
    TableHead,
    TableRow,
    TableCell,
    TableCaption,
} from '../shadcn-components/ui/table';
import {Pagination} from "@mui/material";

const Accounts = (props) => {
    const [error, setError] = useState(null);
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const [accounts, setAccounts] = useState([]);
    const [id, setId] = useState('');
    const [accountName, setAccountName] = useState('');
    const [balance, setBalance] = useState(0.00);
    const [currency, setCurrency] = useState('');
    const [currencies, setCurrencies] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(null);

    const [page, setPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const filteredAccounts = accounts.slice((page - 1) * itemsPerPage, page * itemsPerPage);

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    const resetForm = () => {
        setId('');
        setSelectedAccount(null);
        setAccountName('');
        setBalance(0.00);
        setCurrency('');
    };

    useEffect(() => {
        fetch('http://localhost:8080/account/listAccountDetails', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }).then((response) => {
            if (response.status >= 300 && response.status < 600) {
                response.json().then((data) => {
                    setError(data.message);
                });
            } else {
                response.json().then((data) => {
                    setAccounts(data);
                });
            }
        }).catch((error) => {
            console.error('Error:', error);
            setError('Nepoznata greška');
        });

        fetch('http://localhost:8080/currency')
            .then(response => response.json())
            .then(data => setCurrencies(data))
            .catch(error => console.error('Error fetching currencies:', error));
    }, []);

    async function createAccount(e) {
        e.preventDefault();
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                accountName: accountName,
                balance: balance,
                currency: currency
            }),
        };
        fetch('http://localhost:8080/account/createAccount', options)
            .then((response) => {
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {
                        var newDiv = document.createElement('div');
                        newDiv.className = "border-2 text-white bg-red-400 p-2 rounded-md";
                        newDiv.textContent = data.message;
                        var udiv = document.getElementsByClassName('err2')[0];
                        udiv.insertBefore(newDiv, document.getElementById("errMsg2"));
                    });
                } else {
                    alert('Račun uspješno dodan!');
                    setAccountName('');
                    setBalance(0.00);
                    setCurrency('');
                    window.location.reload();
                }
            })
            .catch((err) => {
            });
    }

    const deleteAccount = async (id) => {
        const isConfirmed = window.confirm("Jeste li sigurni da želite izbrisati račun?");
        if (!isConfirmed) return;

        try {
            const response = await fetch(`http://localhost:8080/account/deleteAccount/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            });

            if (response.status >= 300 && response.status < 600) {
                const data = await response.json().catch(() => null);
                alert(data ? data.message : 'Nepoznata greška');
            } else {
                alert('Račun uspješno izbrisan');
                setAccounts((prevAccounts) => prevAccounts.filter((account) => account.accountName !== accountName));
                window.location.reload();
            }
        } catch (err) {
            console.error('Error:', err);
            setError(err.message || 'Nepoznata greška');
        }
    };

    const openEditDialog = (account) => {
        setId(account.id);
        setSelectedAccount(account);
        setAccountName(account.accountName);
        setBalance(account.balance);
        setCurrency(account.currency);
    };

    const updateAccount = async (e) => {
        e.preventDefault();
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: id,
                accountName: accountName,
                balance: balance,
                currency: currency
            }),
        };
        fetch(`http://localhost:8080/account/updateAccount/${selectedAccount.accountName}`, options)
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
                    alert('Račun uspješno ažuriran!');
                    setSelectedAccount(null);
                    setId('');
                    setAccountName('');
                    setBalance(0.00);
                    setCurrency('');
                    window.location.reload();
                }
            })
    };

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
            <h1 className="text-3xl p-4">Računi</h1>
            <div className="m-6 justify-center items-center ">
                <Table className="border bg-white bg-opacity-90">
                    <TableHeader>
                        <TableRow className="backdrop-blur-xl">
                            <TableHead>Naziv računa</TableHead>
                            <TableHead>Stanje</TableHead>
                            <TableHead></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {filteredAccounts.map((account, index) => (
                            <TableRow key={account.accountName}>
                                <TableCell><Link to={`/racuni/${account.id}`} className=" hover:underline">
                                    {account.accountName}
                                </Link></TableCell>
                                <TableCell>{parseFloat(account.balance).toFixed(2)} {account.currency}</TableCell>
                                <TableCell>
                                    <Button className="mx-2.5" onClick={() => openEditDialog(account)}>Detalji</Button>
                                    <Button id='delete' onClick={() => deleteAccount(account.id)}
                                            className="mx-2.5">Obriši</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            <Dialog open={!!selectedAccount} onOpenChange={() => resetForm()}>
                <DialogContent>
                    <DialogTitle>Uredi račun</DialogTitle>
                    <form onSubmit={updateAccount} className="err">
                        <div id="errMsg">
                            <Label>Naziv računa:</Label>
                            <Input
                                className='mb-4'
                                type="text"
                                id="accountName"
                                value={accountName}
                                onChange={(e) => setAccountName(e.target.value)}
                                required
                            />
                            <Label>Stanje računa:</Label>
                            <Input
                                className='mb-4'
                                type="number"
                                id="balance"
                                value={balance}
                                onChange={(e) => setBalance(e.target.value)}
                                required
                            />
                            <Label>Odaberite valutu:</Label>
                            <Select
                                value={currency}
                                onValueChange={(value) => setCurrency(value)}
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
                        </div>
                        <div>
                            <Button name="update" type="submit" className='my-4'>Spremi</Button>
                        </div>
                    </form>
                </DialogContent>
            </Dialog>

            <Dialog>
                <DialogTrigger asChild>
                    <Button className="fixed bottom-6 right-6 rounded-full w-14 h-14 flex items-center justify-center">
                        <MdOutlineAddBox className="text-white" size={24}/>
                    </Button>
                </DialogTrigger>
                <DialogContent className="err">
                    <DialogTitle>Dodaj račun</DialogTitle>
                    <form onSubmit={createAccount} className="err2">
                        <div id="errMsg2">
                            <Label>Naziv računa:</Label>
                            <Input
                                className='mb-4'
                                type="text"
                                id="accountName"
                                value={accountName}
                                onChange={(e) => setAccountName(e.target.value)}
                                required
                            />
                            <Label>Stanje računa:</Label>
                            <Input
                                className='mb-4'
                                type="number"
                                id="balance"
                                value={balance}
                                onChange={(e) => setBalance(e.target.value)}
                                required
                            />
                            <Label>Odaberite valutu:</Label>
                            <Select
                                value={currency}
                                onValueChange={(value) => setCurrency(value)}
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
                        </div>
                        <div>
                            <Button name="dodaj" type="submit" className='my-4'>Dodaj</Button>
                        </div>
                    </form>
                </DialogContent>
            </Dialog>
            <Pagination
                count={Math.ceil(accounts.length / itemsPerPage)}
                page={page}
                onChange={handlePageChange}
                color="primary"
                size="large"
                className="mt-4"
                shape="rounded"
            />
        </div>
    );
};

export default Accounts;
