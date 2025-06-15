import React, { useState, useEffect } from 'react';
import TaskCompleteForm from './TaskCompleteForm';

function UnosTransakcije() {
    const [jsonInput, setJsonInput] = useState(`{
  "amount": 1000
}`);
    const [tasks, setTasks] = useState([]);
    const [selectedTask, setSelectedTask] = useState(null);

    const startProcess = () => {
        let variables;
        try {
            variables = JSON.parse(jsonInput);
        } catch (e) {
            alert('Neispravan JSON format');
            return;
        }

        fetch('/api/process/start', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(variables)
        })
            .then(res => {
                if (res.ok) {
                    alert('Proces pokrenut');
                    fetchTasks();
                } else {
                    alert('Greška pri pokretanju procesa');
                }
            });
    };

    const fetchTasks = () => {
        fetch('/api/process/tasksWithVariables')
            .then(res => res.json())
            .then(data => {
                const unosTasks = data.filter(task => task.name && task.name.toLowerCase().includes('unos'));
                setTasks(unosTasks);
                setSelectedTask(null);
            })
            .catch(console.error);
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    if (selectedTask) {
        return (
            <div style={{ maxWidth: 600, margin: 'auto', padding: 20 }}>
                <button onClick={() => setSelectedTask(null)} style={{ marginBottom: 20 }}>
                    ← Povratak na listu zadataka za unos
                </button>
                <TaskCompleteForm
                    task={selectedTask}
                    onComplete={() => {
                        alert('Zadatak unosa potvrđen.');
                        fetchTasks();
                    }}
                    onCancel={() => setSelectedTask(null)}
                />
            </div>
        );
    }

    return (
        <div style={{ maxWidth: 600, margin: 'auto', padding: 20 }}>
            <h1>Unos transakcije</h1>

            <label htmlFor="jsonInput">Unesite JSON varijable za proces:</label>
            <textarea
                id="jsonInput"
                value={jsonInput}
                onChange={e => setJsonInput(e.target.value)}
                rows={10}
                style={{ width: '100%', fontFamily: 'monospace', marginTop: 10 }}
            />

            <button onClick={startProcess} style={{ marginTop: 20, marginBottom: 40 }}>
                Pokreni novi proces
            </button>

            <h2>Zadaci za potvrdu unosa</h2>
            {tasks.length === 0 ? (
                <p>Nema zadataka za unos.</p>
            ) : (
                <ul>
                    {tasks.map(task => (
                        <li key={task.id} style={{ marginBottom: 10 }}>
                            <strong>{task.name}</strong> - {task.id}
                            <button onClick={() => setSelectedTask(task)} style={{ marginLeft: 10 }}>
                                Otvori
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default UnosTransakcije;
