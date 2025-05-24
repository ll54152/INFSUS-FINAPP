import React, {useEffect, useState} from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Cookies from "js-cookie";
import HomePage from "./components/HomePage";
import Register from "./components/Register";
import Login from "./components/Login";
import Accounts from "./components/Accounts";
import Transactions from "./components/Transactions";

import Account from "./components/Account";
import Categories from "./components/Categories";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [transactions, setTransactions] = useState([]);
    const [error, setError] = useState('');

    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        fetch("http://localhost:8080/api/logout", {
            method: 'POST',
            credentials: 'include',
        })
            .then(response => {
                if (response.ok) {
                    localStorage.removeItem('username');
                    localStorage.removeItem('name');
                    localStorage.removeItem('userRole');
                    Cookies.remove('user');
                    setIsLoggedIn(false);
                    window.location.replace('/');
                }
            })
            .catch(error => {
                console.error('Logout error:', error);
            });
    };

    useEffect(() => {
        const checkUserAuthentication = async () => {
            const userCookie = Cookies.get('user');
            if (userCookie) {
                setIsLoggedIn(true);
                try {
                    const response = await fetch("http://localhost:8080/transaction/listTransactionDetails", {
                        credentials: 'include',
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                    if (response.status === 401) {
                        handleLogout(); // Automatska odjava ako sesija istekne
                    } else if (response.status >= 300 && response.status < 600) {
                        const data = await response.json();
                        setError(data.message || 'Nepoznata greška');
                    } else {
                        const data = await response.json();
                        setTransactions(data);
                    }
                } catch (error) {
                    console.error('Error fetching transactions:', error);
                    setError('Nepoznata greška prilikom dohvaćanja transakcija');
                }
            }
        };

        const checkTransactions = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/checkTransactions", {
                    method: 'POST',
                    credentials: 'include',
                });
                if (response.status === 401) {
                    handleLogout();
                }
            } catch (error) {
                console.error('Error checking transactions:', error);
            }
        };

        checkUserAuthentication();
        checkTransactions();
    }, []);

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage isLoggedIn={isLoggedIn} onLogout={handleLogout}/>}/>
                {!isLoggedIn && <Route path="/registracija" element={<Register/>}/>}
                {!isLoggedIn && <Route path="/prijava" element={<Login onLogin={handleLogin}/>}/>}
                {isLoggedIn &&
                    <Route path="/racuni" element={<Accounts isLoggedIn={isLoggedIn} onLogout={handleLogout}/>}/>}
                {isLoggedIn &&
                    <Route path="/racuni/:id" element={<Account isLoggedIn={isLoggedIn} onLogout={handleLogout}/>}/>}
                {isLoggedIn && <Route path="/transakcije"
                                      element={<Transactions isLoggedIn={isLoggedIn} onLogout={handleLogout}
                                                             transactions={transactions}/>}/>}
                {isLoggedIn &&
                    <Route path="/kategorije" element={<Categories isLoggedIn={isLoggedIn} onLogout={handleLogout}/>}/>}
            </Routes>
        </BrowserRouter>
    );
}

export default App;
