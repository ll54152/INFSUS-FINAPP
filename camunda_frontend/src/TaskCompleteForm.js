import React, { useState, useEffect } from 'react';

function TaskCompleteForm({ task, onComplete, onCancel }) {
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
                odobreno: { value: approved, type: 'Boolean' }
            }
        };

        fetch(`/api/process/tasks/${task.id}/complete`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (res.ok) {
                    alert('Task completed');
                    onComplete();
                } else {
                    alert('Error completing task');
                }
            });
    };

    return (
        <div style={{ border: '1px solid #ccc', padding: 20, marginTop: 20 }}>
            <h3>Complete Task: {task.name}</h3>

            <h4>Task Variables (read-only)</h4>
            <pre style={{ backgroundColor: '#eee', padding: 10, maxHeight: 200, overflow: 'auto' }}>
        {JSON.stringify(variablesToShow, null, 2)}
      </pre>

            <label>
                Approve:
                <input
                    type="checkbox"
                    checked={approved}
                    onChange={e => setApproved(e.target.checked)}
                    style={{ marginLeft: 10 }}
                />
            </label>
            <br />

            <button onClick={submit} style={{ marginTop: 10 }}>Submit</button>
            <button onClick={onCancel} style={{ marginLeft: 10, marginTop: 10 }}>Cancel</button>
        </div>
    );
}

export default TaskCompleteForm;
