import React, {useState} from 'react';
import Cookies from 'js-cookie';
import {Input} from '../shadcn-components/ui/input'
import {Button} from "../shadcn-components/ui/button";
import {Label} from "../shadcn-components/ui/label";


function Login(props) {
    const onLogin = props.onLogin;
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function login(e) {
        e.preventDefault();


        const body = `username=${username.toLowerCase()}&password=${password}`;
        const options = {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: body,
        };
        fetch('http://localhost:8080/login', options)
            .then((response) => {
                var stariDiv = document.getElementsByClassName('alert-container')[0];
                if (stariDiv && stariDiv.parentElement) {
                    stariDiv.parentElement.removeChild(stariDiv);
                }
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {
                        var newDiv = document.createElement('div');
                        newDiv.className = "border-2 text-white bg-red-400 p-2 rounded-md";
                        const text = data.message === "Bad credentials" ? "Pogrešan email ili lozinka" : data.message;
                        newDiv.textContent = text;
                        var udiv = document.getElementsByClassName('err')[0];
                        udiv.insertBefore(newDiv, document.getElementById("errMsg"));
                    });
                } else {
                    const userRole = response.headers.get('X-Role');
                    Cookies.set('user', 'authenticated');
                    localStorage.setItem('username', username.toLowerCase());
                    localStorage.setItem('name', response.headers.get('X-Name'));
                    localStorage.setItem('userRole', userRole);
                    alert('Prijava uspješna.');
                    onLogin();
                    window.location.replace('/');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            <div className="flex flex-col items-center justify-center py-20">
                <div className=" border rounded-md w-1/2 p-8">
                    <div className="border rounded-md  p-4 bg-white bg-opacity-70">
                        <h2 className='text-2xl my-4 text-center'>Prijava</h2>
                        <form onSubmit={login} className="err">
                            <div id="errMsg">
                                <Label>Email:</Label>
                                <Input className='mb-4'
                                       type="text"
                                       id="username"
                                       value={username}
                                       onChange={(e) => setUsername(e.target.value)}
                                       required
                                />
                                <Label>Lozinka:</Label>
                                <Input className='mb-4'
                                       type="password"
                                       id="password"
                                       value={password}
                                       onChange={(e) => setPassword(e.target.value)}
                                       required
                                />
                            </div>
                            <div>
                                <Button name="prijava" type="submit" className='my-4'>Prijavi se</Button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
