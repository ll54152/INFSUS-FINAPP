import React, { useState, useEffect } from "react";

function ProcessVariablesViewer() {
    const [processKey, setProcessKey] = useState("");
    const [instances, setInstances] = useState([]);
    const [selectedInstanceId, setSelectedInstanceId] = useState(null);
    const [variables, setVariables] = useState({});

    const processOptions = [
        { label: "Proces 1", value: "Process_1r056bh" },
    ];

    useEffect(() => {
        if (!processKey) {
            setInstances([]);
            setSelectedInstanceId(null);
            setVariables({});
            return;
        }

        fetch(`/api/process/instances?processDefinitionKey=${processKey}`)
            .then((res) => res.json())
            .then((data) => {
                setInstances(data);
                setSelectedInstanceId(null);
                setVariables({});
            })
            .catch(console.error);
    }, [processKey]);

    useEffect(() => {
        if (!selectedInstanceId) {
            setVariables({});
            return;
        }

        fetch(`/api/process/instances/${selectedInstanceId}/variables`)
            .then((res) => res.json())
            .then((data) => setVariables(data))
            .catch(console.error);
    }, [selectedInstanceId]);

    return (
        <div>
            <h2>Odaberi proces</h2>
            <select
                value={processKey}
                onChange={(e) => setProcessKey(e.target.value)}
            >
                <option value="">-- odaberi --</option>
                {processOptions.map((opt) => (
                    <option key={opt.value} value={opt.value}>
                        {opt.label}
                    </option>
                ))}
            </select>

            {instances.length > 0 && (
                <>
                    <h3>Instance procesa:</h3>
                    <ul>
                        {instances.map((inst) => (
                            <li key={inst.id}>
                                <button onClick={() => setSelectedInstanceId(inst.id)}>
                                    {inst.id} {inst.ended ? "(završena)" : "(aktivna)"}
                                </button>
                            </li>
                        ))}
                    </ul>
                </>
            )}

            {selectedInstanceId && (
                <>
                    <h3>Varijable instance {selectedInstanceId}:</h3>
                    <ul>
                        {Object.entries(variables).map(([key, val]) => (
                            <li key={key}>
                                {key}: {val.value.toString()} ({val.type})
                            </li>
                        ))}
                    </ul>
                </>
            )}
        </div>
    );
}

export default ProcessVariablesViewer;
