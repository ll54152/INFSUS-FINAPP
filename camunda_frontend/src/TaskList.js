import React, { useState } from 'react';
import TaskCompleteForm from './TaskCompleteForm';

function TaskList({ tasks, refreshTasks }) {
    const [selectedTask, setSelectedTask] = useState(null);

    if (tasks.length === 0) {
        return <p>No tasks assigned.</p>;
    }

    return (
        <div>
            <ul>
                {tasks.map(task => (
                    <li key={task.id} style={{ marginBottom: 10 }}>
                        <strong>{task.name}</strong>
                        <button onClick={() => setSelectedTask(task)} style={{ marginLeft: 10 }}>
                            Complete
                        </button>
                    </li>
                ))}
            </ul>

            {selectedTask && (
                <TaskCompleteForm
                    task={selectedTask}
                    onComplete={() => {
                        setSelectedTask(null);
                        refreshTasks();
                    }}
                    onCancel={() => setSelectedTask(null)}
                />
            )}
        </div>
    );
}

export default TaskList;
