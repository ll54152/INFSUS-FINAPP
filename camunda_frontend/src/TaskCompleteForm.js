import React, {useState} from 'react';
import {
    Typography,
    Paper,
    Box,
    Checkbox,
    FormControlLabel,
    Button,
} from '@mui/material';

function TaskCompleteForm({task, onComplete, onCancel}) {
    const [approved, setApproved] = useState(false);

    const variablesToShow = {};
    if (task.variables) {
        Object.entries(task.variables).forEach(([key, val]) => {
            variablesToShow[key] = val.value;
        });
    }

    const submit = () => {
        const payload = {
            variables: {
                odobreno: {value: approved, type: 'Boolean'},
            },
        };

        fetch(`/api/process/tasks/${task.id}/complete`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload),
        }).then(res => {
            if (res.ok) {
                alert('Task completed');
                onComplete();
            } else {
                alert('Error completing task');
            }
        });
    };

    return (
        <Paper elevation={3} sx={{p: 3}}>
            <Typography variant="h5" gutterBottom>
                {task.name}
            </Typography>

            <Typography variant="subtitle1" gutterBottom>

            </Typography>

            <Box
                component="pre"
                sx={{
                    bgcolor: '#f5f5f5',
                    p: 2,
                    maxHeight: 200,
                    overflow: 'auto',
                    borderRadius: 1,
                    fontFamily: 'monospace',
                }}
            >
                {JSON.stringify(variablesToShow, null, 2)}
            </Box>

            <FormControlLabel
                control={
                    <Checkbox
                        checked={approved}
                        onChange={e => setApproved(e.target.checked)}
                    />
                }
                label="Approve"
                sx={{mt: 2}}
            />

            <Box sx={{mt: 3}}>
                <Button variant="contained" onClick={submit} sx={{mr: 2}}>
                    Submit
                </Button>
                <Button variant="outlined" onClick={onCancel}>
                    Cancel
                </Button>
            </Box>
        </Paper>
    );
}

export default TaskCompleteForm;
