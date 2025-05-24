import React from 'react';
import {Link} from 'react-router-dom';
import {Sheet, SheetContent, SheetTitle, SheetTrigger} from "../shadcn-components/ui/sheet";
import {TfiMenuAlt} from "react-icons/tfi";
import {Button} from "../shadcn-components/ui/button";
import {Label} from "@radix-ui/react-label";
import {Input} from "../shadcn-components/ui/input";

const Header = (props) => {
    const {isLoggedIn, onLogout, onSearch} = props;

    return (
        <div>
            {isLoggedIn ? (
                <div className="p-4 flex items-center justify-between text-sm bg-black text-white">
                    {/* Left: Menu */}
                    <div className="flex items-center">
                        <Sheet>
                            <SheetTrigger className="text-2xl"><TfiMenuAlt/></SheetTrigger>
                            <SheetContent side="left" className="w-[400px] sm:w-[540px] bg-black">
                                <SheetTitle
                                    className="border rounded-md p-4 m-3 hover:bg-accent hover:text-black text-white">
                                    <Link to="/">Početna stranica</Link>
                                </SheetTitle>
                                <SheetTitle
                                    className="border rounded-md p-4 m-3 hover:bg-accent hover:text-black text-white">
                                    <Link to="/racuni">Računi</Link>
                                </SheetTitle>
                                <SheetTitle
                                    className="border rounded-md p-4 m-3 hover:bg-accent hover:text-black text-white">
                                    <Link to="/transakcije">Transakcije</Link>
                                </SheetTitle>
                                <SheetTitle
                                    className="border rounded-md p-4 m-3 hover:bg-accent hover:text-black text-white">
                                    <Link to="/kategorije">Kategorije</Link>
                                </SheetTitle>
                            </SheetContent>
                        </Sheet>
                    </div>
                    {/* Center: Title */}
                    <Label className='text-2xl my-4 text-center flex-1'>FinanceApp</Label>
                    {/* Right: Search + Logout */}
                    <div className="flex items-center gap-4">
                        <Input
                            type="text"
                            placeholder="Pretraži transakcije..."
                            className="w-64 text-black"
                            onChange={e => onSearch && onSearch(e.target.value)}
                        />
                        <Button onClick={onLogout}>Odjava</Button>
                    </div>
                </div>
            ) : (
                <div className="p-4 flex items-center justify-center text-sm text-black-500 bg-gray-500">
                    <Label className='text-2xl my-4 text-center'>FinanceApp</Label>
                </div>
            )}
        </div>
    );
};

export default Header;