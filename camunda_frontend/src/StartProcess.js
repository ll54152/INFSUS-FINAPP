import React from 'react';

function StartProcess({ onStart }) {
    const startProcess = () => {
        fetch('/api/process/start', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: 1000 })
        })
            .then(res => {
                if(res.ok) {
                    alert('Process started');
                    onStart();
                } else {
                    alert('Error starting process');
                }
            })
    };

    return (
        <button onClick={startProcess} style={{ marginBottom: 20 }}>
            Start New Process
        </button>
    );
}

export default StartProcess;
