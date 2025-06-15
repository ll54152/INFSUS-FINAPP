import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import UnosTransakcije from './UnosTransakcije';
import ProvjeraTransakcije from './ProvjeraTransakcije';

function App() {
    return (
        <Router>
            <div style={{ maxWidth: 600, margin: 'auto', padding: 20 }}>
                <nav style={{ marginBottom: 20 }}>
                    <Link to="/" style={{ marginRight: 10 }}>Unos</Link>
                    <Link to="/provjera" >Provjera</Link>
                </nav>
                <Routes>
                    <Route path="/" element={<UnosTransakcije />} />
                    <Route path="/provjera" element={<ProvjeraTransakcije />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
