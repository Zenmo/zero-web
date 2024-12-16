import React, { FormEvent, FunctionComponent, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { PrimeReactProvider } from "primereact/api";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { Project } from "zero-zummon"; // Assuming this is the project model
import { redirectToLogin } from "./use-projects";

export const ProjectForm: FunctionComponent = () => {
    const { projectId } = useParams<{ projectId: string }>();
    const [project, setProject] = useState<Project | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (projectId) {
            const fetchProject = async () => {
                setLoading(true);
                try {
                    const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/projects/${projectId}`, {
                        credentials: "include",
                    });
                    if (response.status === 401) {
                        redirectToLogin();
                        return;
                    }
                    if (response.ok) {
                        const projectData = await response.json();
                        setProject(projectData);
                    } else {
                        alert(`Error fetching project: ${response.statusText}`);
                    }
                } catch (error) {
                    alert((error as Error).message);
                } finally {
                    setLoading(false);
                }
            };
            fetchProject();
        }
    }, [projectId]);

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const method = projectId ? "PUT" : "POST";
            const url = projectId
                ? `${import.meta.env.VITE_ZTOR_URL}/projects/${projectId}`
                : `${import.meta.env.VITE_ZTOR_URL}/projects`;
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(project),
            });
            if (response.status === 401) {
                redirectToLogin();
                return;
            }
            if (response.ok) {
                const projectData = await response.json();
                alert(`Project ${projectId ? "updated" : "created"} successfully!`);
                navigate(`/projects/${projectData.id}`);
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message}`);
            }
        } catch (error) {
            alert((error as Error).message);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setProject((prev) => ({ ...prev, [name]: value } as Project));
    };

    const groupStyle: React.CSSProperties = {
        display: "flex",
        flexDirection: "column",
        gap: "1rem",
        maxWidth: "400px",
        margin: "0 auto",
    };

    return (
        <PrimeReactProvider>
            <div style={groupStyle}>
                <h3>{projectId ? "Edit Project" : "Add Project"}</h3>
                <form onSubmit={handleSubmit} style={groupStyle}>
                    <label htmlFor="name">Name:</label>
                    <InputText
                        id="name"
                        name="name"
                        defaultValue={project?.name || ""}
                        onChange={handleChange}
                        disabled={loading}
                    />
                    <label htmlFor="energiekeRegioId">Energieke Regio ID:</label>
                    <InputText
                        id="energiekeRegioId"
                        name="energiekeRegioId"
                        defaultValue={project?.energiekeRegioId || ""}
                        onChange={handleChange}
                        disabled={loading}
                    />
                    <Button label={loading ? "Loading..." : "Submit"} type="submit" disabled={loading} />
                </form>
            </div>
        </PrimeReactProvider>
    );
};
