import {FunctionComponent} from "react";
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {useProjects} from "./use-projects";
import {PrimeReactProvider} from "primereact/api";
import {Project} from "zero-zummon"

import "primereact/resources/themes/lara-light-cyan/theme.css"
import 'primeicons/primeicons.css'
import {DeleteButton} from "./delete-button";
import {EditButton} from "./edit-button";
import {JsonButton} from "./json-button";
import {DeeplinkButton} from "./deeplink-button"
import {ZeroLayout} from "../components/zero-layout"

export const Projects: FunctionComponent = () => {
    const {loading, projects, changeProject, removeProject} = useProjects()
    return (
        <PrimeReactProvider>
            <DataTable
                value={projects}
                loading={loading}
                sortField="created"
                sortOrder={-1}
                filterDisplay="row"
            >
                <Column field="name" header="Name" sortable filter />
                <Column field="energiekeRegioId" header="Energie Regio Id" sortable filter />

                <Column body={(project: Project) => (
                    <div css={{
                        display: 'flex',
                        '> *': {
                            margin: `${1/6}rem`
                        },
                    }}>
                        <JsonButton surveyId={project.id}/>
                        <DeleteButton surveyId={project.id} onDelete={removeProject}/>
                        <EditButton surveyId={project.id}/>
                        <DeeplinkButton surveyId={project.id}/>
                    </div>
                )}/>
            </DataTable>
        </PrimeReactProvider>
    )
}
