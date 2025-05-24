import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import Transactions from "./Transactions";
import Header from "./Header";

const Account = (props) => {
    const [error, setError] = useState(null);
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;
    const {id} = useParams();
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:8080/account/listAccountsTransactions/${id}`, {
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
                    setTransactions(data);
                });
            }
        }).catch((err) => {
            console.error('Error:', error);
            setError('Nepoznata greška');
        });
    }, [id]);

    return (
        <div>
            <Transactions isLoggedIn={isLoggedIn} onLogout={onLogout} transactions={transactions}></Transactions>
        </div>
    );
};

export default Account;
