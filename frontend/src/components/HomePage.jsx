import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Button} from "../shadcn-components/ui/button";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "../shadcn-components/ui/table";
import Header from "./Header";
import Cookies from "js-cookie";

const HomePage = (props) => {
    const {isLoggedIn, onLogout} = props;
    const [error, setError] = useState(null);
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        const checkUserAuthentication = async () => {
            const userCookie = Cookies.get('user');
            if (userCookie) {
                try {
                    const response = await fetch('http://localhost:8080/transaction/listTodaysTransactions', {
                        credentials: 'include',
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                    if (!response.ok) {
                        throw new Error('Unauthorized');
                    }
                    const data = await response.json();
                    setTransactions(data);
                } catch (error) {
                    console.error('Error fetching transactions:', error);
                    setError('Nepoznata greška prilikom dohvaćanja transakcija');
                }
            }
        };
        checkUserAuthentication();
    }, []);

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            {isLoggedIn ? (
                <div>
                    <Header isLoggedIn={isLoggedIn} onLogout={onLogout}></Header>
                    <h1 className="text-3xl p-4">Današnje transakcije</h1>
                    <div className="m-6 justify-center items-center">
                        <Table className="border bg-white bg-opacity-90">
                            <TableHeader>
                                <TableRow className="backdrop-blur-xl">
                                    <TableHead>Naziv transakcije</TableHead>
                                    <TableHead>Iznos</TableHead>
                                    <TableHead>Kategorija</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {transactions.map(transaction => (
                                    <TableRow key={transaction.id}>
                                        <TableCell>{transaction.transactionName}</TableCell>
                                        <TableCell>{transaction.typeOfTransaction === "rashod" ? `-` : ``}{transaction.transactionAmount} {transaction.currency}</TableCell>
                                        <TableCell>{transaction.category}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </div>
                </div>
            ) : (
                <div>
                    <div className="pt-72"></div>
                    <div
                        className="flex flex-col items-center object-center justify-center bg-black bg-opacity-80 py-6">
                        <h1 className="text-6xl mb-4 text-white">FinanceApp</h1>
                        <div>
                            <Button className="p-4 m-4 border-2">
                                <Link to="/registracija">Registriraj se</Link>
                            </Button>
                            <Button className="p-4 m-4 border-2">
                                <Link to="/prijava">Prijavi se</Link>
                            </Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default HomePage;
