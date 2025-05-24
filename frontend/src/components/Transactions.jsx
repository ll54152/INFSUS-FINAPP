import React, {useEffect, useState} from 'react';
import Header from "./Header";
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
    TableBody,
    TableCell,
    TableFooter,
    TableHead,
    TableHeader,
    TableRow
} from "../shadcn-components/ui/table";
import {TabsTrigger} from "../shadcn-components/ui/tabs";
import {Tabs, TabsContent, TabsList} from "@radix-ui/react-tabs";
import AddCategory from "./AddCategory";
import {Pagination} from "@mui/material";
import AddTransaction from "./AddTransaction";
import {format} from "date-fns";

const Transactions = (props) => {
    const getTodayDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const [error, setError] = useState(null);
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const transactions = props.transactions || [];
    const [setTransactions] = useState([]);
    const [id, setId] = useState('');
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
    const [accountName2, setAccountName2] = useState('');
    const [accounts, setAccounts] = useState([]);
    const [selectedTransaction, setSelectedTransaction] = useState(null);

    const [page, setPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);

    const [searchTerm, setSearchTerm] = useState('');

    const sortedTransactions = [...transactions].sort((a, b) => {
        const dateA = new Date(a.dateOfTransaction);
        const dateB = new Date(b.dateOfTransaction);
        return dateB - dateA;
    });

    const filteredBySearch = sortedTransactions.filter(t =>
            t.transactionName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const paginatedTransactions = filteredBySearch.slice((page - 1) * itemsPerPage, page * itemsPerPage);

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    const resetForm = () => {
        setSelectedTransaction(null);
        setId('');
        setTransactionName('');
        setDateOfTransaction(getTodayDate());
        setNote('');
        setTypeOfTransaction('');
        setTransactionAmount('');
        setCategory('');
        setAccountName('');
        setCurrency('');
    };

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

    const filterTransactions = (type) => {
        const today = new Date(getTodayDate());
        return filteredBySearch.filter(transaction => {
            const transactionDate = new Date(transaction.dateOfTransaction);
            if (type === "toDo") {
                return transactionDate > today;
            } else if (type === "done") {
                return transactionDate <= today;
            }
            return true;
        });
    };

    const deleteTransaction = async (id) => {
        const isConfirmed = window.confirm("Jeste li sigurni da želite izbrisati transakciju?");
        if (!isConfirmed) return;

        try {
            const response = await fetch(`http://localhost:8080/transaction/deleteTransaction/${id}`, {
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
                alert('Transakcija uspješno izbrisana');
                window.location.reload();
            }
        } catch (err) {
            console.error('Error:', err);
            setError(err.message || 'Nepoznata greška');
        }
    };

    const openEditDialog = (transaction) => {
        const formattedDate = format(new Date(transaction.dateOfTransaction), 'yyyy-MM-dd');

        setSelectedTransaction(transaction);
        setId(transaction.id);
        setTransactionName(transaction.transactionName);
        setDateOfTransaction(formattedDate);
        setNote(transaction.note);
        setTypeOfTransaction(transaction.typeOfTransaction);
        setTransactionAmount(transaction.transactionAmount);
        setCategory(transaction.category);
        setAccountName(transaction.accountName);
        setCurrency(transaction.currency);
    };

    const updateTransaction = async (e) => {
        e.preventDefault();
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: id,
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
        fetch(`http://localhost:8080/transaction/updateTransaction/${selectedTransaction.id}`, options)
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
                    alert('Transakcija uspješno ažurirana!');
                    setSelectedTransaction(null);
                    setId('');
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
    };

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout} onSearch={setSearchTerm}/>
            <Tabs defaultValue="all" className=" flex-col items-center justify-center mx-6">
                <TabsList className="my-6">
                    <TabsTrigger className="text-xl" value="all">Sve transakcije</TabsTrigger>
                    <TabsTrigger className="text-xl" value="toDo">Zakazane transakcije</TabsTrigger>
                    <TabsTrigger className="text-xl" value="done">Obavljene transakcije</TabsTrigger>
                </TabsList>
                <TabsContent value="all">
                    <TableComponent
                        transactions={filterTransactions("all").slice((page - 1) * itemsPerPage, page * itemsPerPage)}
                        deleteTransaction={deleteTransaction} openEditDialog={openEditDialog}/>
                </TabsContent>
                <TabsContent value="toDo">
                    <TableComponent
                        transactions={filterTransactions("toDo").slice((page - 1) * itemsPerPage, page * itemsPerPage)}
                        deleteTransaction={deleteTransaction} openEditDialog={openEditDialog}/>
                </TabsContent>
                <TabsContent value="done">
                    <TableComponent
                        transactions={filterTransactions("done").slice((page - 1) * itemsPerPage, page * itemsPerPage)}
                        deleteTransaction={deleteTransaction} openEditDialog={openEditDialog}/>
                </TabsContent>
            </Tabs>

            <Dialog open={!!selectedTransaction} onOpenChange={() => resetForm()}>
                <DialogContent className="max-h-[90vh] overflow-auto p-4">
                    <DialogTitle>Uredi transakciju</DialogTitle>
                    <form onSubmit={updateTransaction} className="err">
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
                                <AddCategory></AddCategory>
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
                <DialogContent className="max-h-[90vh] overflow-auto p-4">
                    <DialogTitle>Dodaj transakciju</DialogTitle>
                    <AddTransaction></AddTransaction>
                </DialogContent>
            </Dialog>
            <Pagination
                count={Math.ceil(filteredBySearch.length / itemsPerPage)}
                page={page}
                onChange={handlePageChange}
                color="primary"
                size="large"
                shape="rounded"
            />
        </div>
    );
};

const TableComponent = ({transactions, deleteTransaction, openEditDialog}) => {

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const day = date.getDate();
        const month = date.getMonth() + 1;
        const year = date.getFullYear();

        return `${day}.${month}.${year}`;
    };

    return (
        <Table className="border bg-white bg-opacity-90">
            <TableHeader>
                <TableRow className="backdrop-blur-xl">
                    <TableHead>Naziv transakcije</TableHead>
                    <TableHead>Iznos</TableHead>
                    <TableHead>Datum transakcije</TableHead>
                    <TableHead></TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {transactions.map((transaction) => (
                    <TableRow key={transaction.id}>
                        <TableCell>{transaction.transactionName}</TableCell>
                        <TableCell>{transaction.typeOfTransaction === "rashod" ? `-` : ``}{transaction.transactionAmount} {transaction.currency}</TableCell>
                        <TableCell>{formatDate(transaction.dateOfTransaction)}</TableCell>
                        <TableCell>
                            <Button className="mx-2.5" onClick={() => openEditDialog(transaction)}>Detalji</Button>
                            <Button id='delete' onClick={() => deleteTransaction(transaction.id)}
                                    className="mx-2.5">Obriši</Button>
                        </TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
}

export default Transactions;