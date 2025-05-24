import {DialogContent, DialogTrigger, Dialog, DialogTitle} from "../shadcn-components/ui/dialog";
import {Label} from "../shadcn-components/ui/label";
import {Input} from "../shadcn-components/ui/input";
import {Button} from "../shadcn-components/ui/button";
import React, {useState, useEffect} from "react";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "../shadcn-components/ui/table";
import Header from "./Header";
import {MdOutlineAddBox} from "react-icons/md";
import {Pagination} from "@mui/material";

const Categories = (props) => {
    const [categoryName, setCategoryName] = useState('');
    const [error, setError] = useState(null);
    const [categories, setCategories] = useState([]);
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;

    const [page, setPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const filteredCategories = categories.slice((page - 1) * itemsPerPage, page * itemsPerPage);

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    useEffect(() => {
        fetch('http://localhost:8080/category/listCategoryDetails', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => setCategories(data))
            .catch(error => console.error('Error fetching categories:', error));
    }, []);

    const createCategory = async () => {
        const options = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                categoryName: categoryName
            }),
        };
        fetch('http://localhost:8080/category/createCategory', options)
            .then((response) => {
                if (response.status >= 300 && response.status < 600) {
                    response.json().then((data) => {
                        setError(data.message);
                    });
                } else {
                    alert('Kategorija uspješno dodana!');
                    setCategoryName('');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                setError('Došlo je do greške. Pokušajte ponovo.');
            });
    };

    const deleteCategory = async (id) => {
        const isConfirmed = window.confirm("Jeste li sigurni da želite izbrisati kategoriju?");
        if (!isConfirmed) return;

        try {
            const response = await fetch(`http://localhost:8080/category/deleteCategory/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            });

            if (response.status >= 300 && response.status < 600) {
                const data = await response.json().catch(() => null); // Catch JSON parse errors
                alert(data ? data.message : 'Nepoznata greška');
            } else {
                alert('Kategorija uspješno izbrisana');
                setCategories(prevCategories => prevCategories.filter(category => category.id !== id));
            }
        } catch (err) {
            console.error('Error:', err);
            setError(err.message || 'Nepoznata greška');
        }
    };

    return (
        <div className="min-h-screen bg-[url('/pocetna.jpg')] bg-cover bg-fixed">
            <Header isLoggedIn={isLoggedIn} onLogout={onLogout}/>
            <h1 className="text-3xl p-4">Kategorije</h1>
            <div className="m-6 justify-center items-center ">
                <Table className="border bg-white bg-opacity-90">
                    <TableHeader>
                        <TableRow className="backdrop-blur-xl">
                            <TableHead>Naziv kategorije</TableHead>
                            <TableHead></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {filteredCategories.map((category, index) => (
                            <TableRow key={category.categoryName}>
                                <TableCell>
                                    {category.categoryName}
                                </TableCell>
                                <TableCell>
                                    <Button id='delete' onClick={() => deleteCategory(category.id)}
                                            className="mx-2.5">Obriši</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            <Dialog>
                <DialogTrigger asChild>
                    <Button className="fixed bottom-6 right-6 rounded-full w-14 h-14 flex items-center justify-center">
                        <MdOutlineAddBox className="text-white" size={24}/>
                    </Button>
                </DialogTrigger>
                <DialogContent className="err">
                    <DialogTitle>Dodaj kategoriju</DialogTitle>
                    <form onSubmit={createCategory} className="err2">
                        <div id="errMsg2">
                            <Label>Naziv kategorije:</Label>
                            <Input
                                className='my-4'
                                type="text"
                                id="accountName"
                                value={categoryName}
                                onChange={(e) => setCategoryName(e.target.value)}
                                required
                            />

                        </div>
                        <div>
                            <Button name="dodaj" type="submit" className='my-4'>Dodaj</Button>
                        </div>
                    </form>
                </DialogContent>
            </Dialog>
            <Pagination
                count={Math.ceil(categories.length / itemsPerPage)}
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

export default Categories;
