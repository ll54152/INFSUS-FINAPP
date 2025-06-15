import React, { useState, useEffect } from 'react';
import {
    Container,
    Typography,
    TextField,
    Button,
    List,
    ListItem,
    ListItemText,
    ListItemSecondaryAction,
    IconButton,
    Paper,
    Box,
} from '@mui/material';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
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
        } catch {
            alert('Neispravan JSON format');
            return;
        }

        fetch('/api/process/start', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(variables),
        }).then(res => {
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
                const unosTasks = data.filter(task =>
                    task.name?.toLowerCase().includes('unos')
                );
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
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Button
                    variant="outlined"
                    onClick={() => setSelectedTask(null)}
                    sx={{ mb: 3 }}
                >
                    ← Povratak na listu zadataka za unos
                </Button>

                <TaskCompleteForm
                    task={selectedTask}
                    onComplete={() => {
                        alert('Zadatak unosa potvrđen.');
                        fetchTasks();
                    }}
                    onCancel={() => setSelectedTask(null)}
                />
            </Container>
        );
    }

    return (
        <Container maxWidth="sm" sx={{ mt: 4 }}>
            <Typography variant="h4" gutterBottom>
                Unos transakcije
            </Typography>

            <TextField
                label="Unesite JSON varijable za proces"
                multiline
                minRows={10}
                value={jsonInput}
                onChange={e => setJsonInput(e.target.value)}
                fullWidth
                variant="outlined"
                sx={{ fontFamily: 'monospace', mb: 3 }}
            />

            <Button variant="contained" onClick={startProcess} sx={{ mb: 4 }}>
                Pokreni novi proces
            </Button>

            <Typography variant="h5" gutterBottom>
                Zadaci za potvrdu unosa
            </Typography>

            {tasks.length === 0 ? (
                <Typography>Nema zadataka za unos.</Typography>
            ) : (
                <Paper elevation={2}>
                    <List>
                        {tasks.map(task => (
                            <ListItem key={task.id} divider>
                                <ListItemText primary={task.name} secondary={task.id} />
                                <ListItemSecondaryAction>
                                    <IconButton
                                        edge="end"
                                        aria-label="open"
                                        onClick={() => setSelectedTask(task)}
                                    >
                                        <OpenInNewIcon />
                                    </IconButton>
                                </ListItemSecondaryAction>
                            </ListItem>
                        ))}
                    </List>
                </Paper>
            )}
        </Container>
    );
}

export default UnosTransakcije;
