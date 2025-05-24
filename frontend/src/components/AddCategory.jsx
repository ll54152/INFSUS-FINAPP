import {DialogContent, DialogTrigger, Dialog, DialogTitle} from "@radix-ui/react-dialog";
import {Label} from "../shadcn-components/ui/label";
import {Input} from "../shadcn-components/ui/input";
import {Button} from "../shadcn-components/ui/button";
import React, {useState, useEffect} from "react";

const AddCategory = ({updateCategories}) => {
    const [categoryName, setCategoryName] = useState('');
    const [error, setError] = useState(null);
    const [categories, setCategories] = useState([]);

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
                    updateCategories(categoryName);
                    setCategoryName('');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                setError('Došlo je do greške. Pokušajte ponovo.');
            });
    };


    return (
        <div>
            <Dialog>
                <DialogTrigger asChild>
                    <Button className="mb-4">
                        Dodaj kategoriju
                    </Button>
                </DialogTrigger>
                <DialogContent className="border-2 border-gray-300 rounded-md p-4">
                    <DialogTitle>Dodaj kategoriju</DialogTitle>
                    <div className="err">
                        {error && <div className="border-2 text-white bg-red-400 p-2 rounded-md">{error}</div>}
                        <Label htmlFor="categoryName">Naziv kategorije:</Label>
                        <Input
                            className="my-4"
                            type="text"
                            id="categoryName"
                            value={categoryName}
                            onChange={(e) => setCategoryName(e.target.value)}
                            required
                        />
                        <Button onClick={createCategory}>
                            Dodaj
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    );
};

export default AddCategory;
