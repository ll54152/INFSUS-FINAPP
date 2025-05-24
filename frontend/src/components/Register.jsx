import React, {useState} from "react";
import axios from "axios";
import {Label} from "@radix-ui/react-label";
import {Input} from "../shadcn-components/ui/input";
import {Button} from "../shadcn-components/ui/button";

function Register() {

    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");


    async function register(event) {
        event.preventDefault();
        try {
            await axios.post("http://localhost:8080/api/register", {
                name: name,
                surname: surname,
                email: email,
                password: password,
            });
            alert("Registracija uspješna!");
            window.location.replace('/prijava');

        } catch (err) {
            var newDiv = document.createElement('div');
            newDiv.className = "border-2 text-white bg-red-400 p-2 rounded-md";
            newDiv.textContent = err.response.data.message;
            var udiv = document.getElementsByClassName('err')[0];
            udiv.insertBefore(newDiv, document.getElementById("errMsg"));
        }
    }

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            <div className="flex flex-col items-center justify-center py-20">
                <div className="border rounded-md w-1/2 p-8">
                    <div className="border rounded-md  p-4 bg-white bg-opacity-70">
                        <h2 className='text-2xl my-4 text-center'>Registracija</h2>

                        <form className="err">
                            <div id="errMsg">
                                <Label>Ime:</Label>
                                <Input type="text" className="form-control" id="personName" placeholder="Unesite ime"

                                       value={name}
                                       onChange={(event) => {
                                           setName(event.target.value);
                                       }}
                                />

                            </div>
                            <div>
                                <Label>Prezime:</Label>
                                <Input type="text" className="form-control" id="personSurname"
                                       placeholder="Unesite prezime"

                                       value={surname}
                                       onChange={(event) => {
                                           setSurname(event.target.value);
                                       }}
                                />

                            </div>

                            <div>
                                <Label>Email:</Label>
                                <Input type="email" className="form-control" id="email" placeholder="Unesite email"

                                       value={email}
                                       onChange={(event) => {
                                           setEmail(event.target.value);
                                       }}

                                />

                            </div>

                            <div>
                                <Label>Lozinka</Label>
                                <Input type="password" className="form-control" id="password"
                                       placeholder="Unesite lozinku"

                                       value={password}
                                       onChange={(event) => {
                                           setPassword(event.target.value);
                                       }}

                                />
                            </div>

                            <Button type="submit" onClick={register} className='my-4'>Registriraj se</Button>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Register;