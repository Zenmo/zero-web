import React, { useEffect, FunctionComponent, useState } from "react";
import { Project, projectsFromJson } from "zero-zummon";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { EditButton } from "./edit-button";

type UserProjectsListProps = {
    userId?: string;
};

export const UserProjectsList: FunctionComponent<UserProjectsListProps> = ({
    userId,
}) => {
    const [projects, setProjects] = useState<Project[]>([]);

    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/users/${userId}/projects`, {
                    credentials: "include",
                });
                if (!response.ok) return

                setProjects(projectsFromJson(await response.text()))
            } catch (error) {
                console.error("Error fetching projects:", error);
            }
        };

        fetchProjects();
    });
    
    return (
        <div>
            <h3>Current Projects</h3>
            <DataTable value={projects} responsiveLayout="scroll">
                <Column field="name" header="Project Name" />
                <Column
                    body={(project: Project) => (
                        <EditButton type="projects" id={project.id}/>
                    )}
                    header="Actions"
                />
            </DataTable>
        </div>
    );
};
