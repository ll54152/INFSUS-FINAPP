import React, { useEffect, useState } from 'react';
import {
    Container,
    Typography,
    List,
    ListItem,
    ListItemText,
    ListItemSecondaryAction,
    IconButton,
    Button,
    Paper,
} from '@mui/material';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import TaskCompleteForm from './TaskCompleteForm';

function ProvjeraTransakcije() {
    const [tasks, setTasks] = useState([]);
    const [selectedTask, setSelectedTask] = useState(null);

    const fetchTasks = () => {
        fetch('/api/process/tasksWithVariables')
            .then(res => res.json())
            .then(data => {
                const provjeraTasks = data.filter(task =>
                    task.name?.toLowerCase().includes('provjera')
                );
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
            <Container maxWidth="sm" sx={{ mt: 4 }}>
                <Button
                    variant="outlined"
                    onClick={() => setSelectedTask(null)}
                    sx={{ mb: 3 }}
                >
                    ← Povratak na listu zadataka za provjeru
                </Button>

                <TaskCompleteForm
                    task={selectedTask}
                    onComplete={() => {
                        alert('Zadatak provjere potvrđen.');
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
                Provjera transakcije - zadaci za potvrdu
            </Typography>

            {tasks.length === 0 ? (
                <Typography>Nema zadataka za provjeru.</Typography>
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

export default ProvjeraTransakcije;
