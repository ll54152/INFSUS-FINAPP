import React, { useEffect, useState } from 'react';
import TaskCompleteForm from './TaskCompleteForm';

function ProvjeraTransakcije() {
    const [tasks, setTasks] = useState([]);
    const [selectedTask, setSelectedTask] = useState(null);

    const fetchTasks = () => {
        fetch('/api/process/tasksWithVariables')
            .then(res => res.json())
            .then(data => {
                const provjeraTasks = data.filter(task => task.name && task.name.toLowerCase().includes('provjera'));
                setTasks(provjeraTasks);
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
                    ← Povratak na listu zadataka za provjeru
                </button>
                <TaskCompleteForm
                    task={selectedTask}
                    onComplete={() => {
                        alert('Zadatak provjere potvrđen.');
                        fetchTasks();
                    }}
                    onCancel={() => setSelectedTask(null)}
                />
            </div>
        );
    }

    return (
        <div style={{ maxWidth: 600, margin: 'auto', padding: 20 }}>
            <h1>Provjera transakcije - zadaci za potvrdu</h1>
            {tasks.length === 0 ? (
                <p>Nema zadataka za provjeru.</p>
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

export default ProvjeraTransakcije;
