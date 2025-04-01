import React, {FormEvent, FunctionComponent, useEffect, useRef, useState} from "react";
import {useParams} from "react-router-dom";
import {PrimeReactProvider} from "primereact/api";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import {Project, projectsFromJson, User} from "zero-zummon";
import {redirectToLogin} from "./use-users";
import {ProjectsDropdown} from "./projects-dropdown";
import {UserProjectsList} from "./user-projects-list";
import {Toast} from "primereact/toast";
import {Content} from "../components/Content";
import {ActionButtonPair} from "../components/helpers/ActionButtonPair";

export const UserForm: FunctionComponent = () => {
    const {userId} = useParams<{ userId: string }>();
    const [user, setUser] = useState<User | null>(null);
    const [originalData, setOriginalData] = useState<User | null>(null);
    const [selectedProjects, setSelectedProjects] = useState<Project[]>([]);
    const [userProjects, setUserProjects] = useState<Project[]>([]);
    const msgs = useRef<Toast>(null);

    const [loading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);

    const handleCancel = () => {
        if (originalData) { // Revert to original data
            setUser(originalData);
            setSelectedProjects(userProjects)

        }
        setIsEditing(false);
    };

    const handleEditToggle = () => {
        setIsEditing(true);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value, type, checked} = e.target;
        setUser((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        } as User));
    };

    const transformProjects = (projects: any[]): Project[] => {
        const jsonString = JSON.stringify(projects);
        return projectsFromJson(jsonString);
    };

    useEffect(() => {
        if (userId) {
            const fetchUser = async () => {
                setLoading(true);
                try {
                    const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/users/${userId}/projects`, {
                        credentials: "include",
                    });
                    if (response.status === 401) {
                        redirectToLogin();
                        return;
                    }
                    if (response.ok) {
                        const userData = await response.json();
                        setUser(userData);
                        setOriginalData(userData);
                        setUserProjects(userData.projects)
                        const formattedProjects = transformProjects(userData.projects)
                        setSelectedProjects(formattedProjects)
                    } else {
                        alert(`Error fetching user: ${response.statusText}`);
                    }
                } catch (error) {
                    alert((error as Error).message);
                } finally {
                    setLoading(false);
                }
            };
            fetchUser();
        } else {
            setIsEditing(true);
        }
    }, [userId]);

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const sendUser = JSON.stringify({
                ...user,
                projects: selectedProjects.map((project) => ({
                    id: project.id.toString(),
                    name: project.name,
                })),
            })
            const method = userId ? "PUT" : "POST";
            const url = `${import.meta.env.VITE_ZTOR_URL}/users`
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: sendUser,
            });

            if (response.status === 401) {
                return;
            }
            if (response.ok) {
                msgs.current?.show([
                    {
                        sticky: true,
                        severity: "success",
                        summary: "Success",
                        detail: "User saved successfully.",
                        closable: true
                    },
                ]);
                setUserProjects(selectedProjects);
            } else {
                msgs.current?.show([
                    {
                        sticky: true,
                        severity: 'error',
                        summary: 'Error',
                        detail: `Error: ${response.statusText}`,
                        closable: false
                    },
                ]);
            }
        } finally {
            setIsEditing(false);
            setLoading(false);
        }
    };

    return (
        <PrimeReactProvider>
            <Content>
                <Toast ref={msgs}/>
                <div className={'row g-10 mb-10'}>
                    <div className={'col-5'}>
                        <div className={'card card-custom border border-0 shadow-none rounded rounded-4'}>
                            <div className={'card-header border border-0  py-3'}>
                                <h3 className={'card-title fs-3 fw-bolder text-dark'}>
                                    {userId ? "Edit User" : "Add User"}
                                </h3>
                            </div>
                            <div className={'card-body'}>
                                <form
                                    onSubmit={handleSubmit}
                                    className={'form d-flex flex-column gap-5'}
                                >
                                    <div className='fv-row mb-5'>
                                        <label htmlFor="name" className={'form-label'}>Keycloak ID:</label>
                                        <InputText
                                            id="id"
                                            name="id"
                                            value={user?.id || ""}
                                            onChange={handleInputChange}
                                            disabled={!isEditing}
                                            className={'form-control bg-transparent'}
                                        />
                                    </div>
                                    <div className='fv-row mb-5'>
                                        <label htmlFor="name" className={'form-label'}>Note:</label>
                                        <InputText
                                            id="note"
                                            name="note"
                                            value={user?.note || ""}
                                            onChange={handleInputChange}
                                            disabled={!isEditing}
                                            className={'form-control bg-transparent'}
                                        />
                                    </div>
                                    <div className={'row g-5 align-items-center'}>
                                        <div className={'col-4'}>
                                            <label htmlFor="isAdmin"
                                                   className='form-check form-check-custom form-check-solid align-items-start'>
                                                <input
                                                    className='form-check-input me-3'
                                                    type="checkbox"
                                                    id="isAdmin"
                                                    name="isAdmin"
                                                    checked={user?.isAdmin || false}
                                                    onChange={handleInputChange}
                                                    disabled={!isEditing}
                                                />
                                                <span className={'form-label'}>Admin</span>
                                            </label>
                                        </div>
                                        <div className={'col-8'}>
                                            <ProjectsDropdown
                                                selectedProjects={selectedProjects}
                                                onChange={setSelectedProjects}
                                                disabled={!isEditing}
                                            />
                                        </div>
                                    </div>


                                </form>
                            </div>
                            <div className={'card-footer py-3 border border-0 d-flex justify-content-end'}>
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
                                            negativeText={loading ? "Saving..." : "Save"}
                                            negativeDisabled={loading}
                                            positiveDisabled={loading}
                                            negativeButtonType={'submit'}
                                            className={'d-flex flex-row gap-5'}
                                        />
                                    </>
                                ) : (
                                    <Button label="Edit" onClick={handleEditToggle} type="button"
                                            disabled={loading}
                                            className="rounded rounded-3"/>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className={'col-7'}>
                        <UserProjectsList projects={userProjects}/>
                    </div>
                </div>
            </Content>
        </PrimeReactProvider>
    );
};
