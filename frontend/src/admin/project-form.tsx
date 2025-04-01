import React, {FormEvent, FunctionComponent, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {PrimeReactProvider} from "primereact/api";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import {Project} from "zero-zummon";
import {redirectToLogin} from "./use-projects";
import {Content} from "../components/Content";
import {ActionButtonPair} from "../components/helpers/ActionButtonPair";

export const ProjectForm: FunctionComponent = () => {
    const {projectId} = useParams<{ projectId: string }>();
    const [project, setProject] = useState<Project | null>(null);
    const [originalData, setOriginalData] = useState<Project | null>(null);

    const [loading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();

    const handleCancel = () => {
        if (originalData) {
            setProject(originalData); // Revert to original data
        }
        setIsEditing(false);
    };

    const handleEditToggle = () => {
        setIsEditing(true);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setProject((prev) => ({...prev, [name]: value} as Project));
    };

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
                        setOriginalData(projectData);
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
        } else {
            setIsEditing(true);
        }
    }, [projectId]);

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const method = projectId ? "PUT" : "POST";
            const url = `${import.meta.env.VITE_ZTOR_URL}/projects`
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
                navigate(`/projects`);
            } else {
                alert(`Error fetching project: ${response.statusText}`);
            }
        } finally {
            setIsEditing(false);
            setLoading(false);
        }
    };

    return (
        <PrimeReactProvider>
            <Content>
                <div className={'d-flex flex-center flex-column'}>
                    <h3>{projectId ? "Edit Project" : "Add Project"}</h3>
                    <form
                        onSubmit={handleSubmit}
                        className={'form w-300px d-flex flex-column gap-5'}
                    >
                        <div className='fv-row'>
                            <label htmlFor="name" className={'form-label'}>Name:</label>
                            <InputText
                                id="name"
                                name="name"
                                value={project?.name || ""}
                                onChange={handleInputChange}
                                disabled={!isEditing}
                                className={'form-control bg-transparent'}
                            />
                        </div>
                        <div className='fv-row'>
                            <label htmlFor="energiekeRegioId" className={'form-label'}>Energieke Regio ID:</label>
                            <InputText
                                id="energiekeRegioId"
                                name="energiekeRegioId"
                                value={project?.energiekeRegioId?.toString() || ""}
                                onChange={handleInputChange}
                                disabled={!isEditing}
                                className={'form-control bg-transparent'}
                            />
                        </div>

                        <div className={'d-flex justify-content-end mt-5 w-100'}>
                            {isEditing ? (
                                <>
                                    <ActionButtonPair
                                        positiveText={'Cancel'}
                                        positiveIcon={undefined}
                                        positiveAction={handleCancel}
                                        positiveClassName='btn btn-sm bg-secondary border border-0'
                                        positiveSeverity={'secondary'}
                                        negativeSeverity={undefined}
                                        showNegative={true}
                                        negativeButtonType={'submit'}
                                        negativeText={loading ? "Saving..." : "Save"}
                                        negativeDisabled={loading}
                                        positiveDisabled={loading}
                                        className={'d-flex flex-row gap-5'}
                                    />
                                </>
                            ) : (
                                <Button label="Edit" onClick={handleEditToggle} type="button" disabled={loading}
                                        className="rounded rounded-3"/>
                            )}
                        </div>
                    </form>
                </div>
            </Content>
        </PrimeReactProvider>
    );
};
