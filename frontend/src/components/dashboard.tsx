import React, {FunctionComponent} from "react";

import "primereact/resources/themes/lara-light-cyan/theme.css"
import 'primeicons/primeicons.css'
import {ToggleButton} from "primereact/togglebutton"

import {useState} from "react";
import {Users} from "../admin/users";
import {Projects} from "../admin/projects";
import {Surveys} from "../admin/surveys";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {useUsers} from "../admin/use-users";
import {useProjects} from "../admin/use-projects";
import {useSurveys} from "../admin/use-surveys";

export const Dashboard: FunctionComponent = () => {
    const {loadingUsers, users, changeUser, removeUser} = useUsers()
    const {loadingProjects, projects, changeProject, removeProject} = useProjects()
    const {loading, surveys, changeSurvey, removeSurvey} = useSurveys()

    const [selectedUser, setSelectedUser] = useState(null);
    const [selectedProject, setSelectedProject] = useState(null);
    const [selectedSurvey, setSelectedSurvey] = useState(null);

    const handleUserSelect = (user: any ) => {
        setSelectedUser(user.value);
        setSelectedProject(null);
    };

    const handleProjectSelect = (project: any) => {
        setSelectedProject(project.value);
    };

    const [showUsers, setShowUsers] = useState(true);
    const [showProjects, setShowProjects] = useState(true);
    const [showSurveys, setShowSurveys] = useState(true);

    return (

       <div>
           <div>
           <ToggleButton checked={showUsers} onChange={() => setShowUsers(!showUsers)} onLabel="Hide Users"
                         offLabel="Show Users" css={{padding: '1em'}}/>
           <ToggleButton checked={showProjects} onChange={() => setShowProjects(!showProjects)} onLabel="Hide Projects"
                         offLabel="Show Projects" css={{padding: '1em'}}/>
           <ToggleButton checked={showSurveys} onChange={() => setShowSurveys(!showSurveys)} onLabel="Hide Surveys"
                         offLabel="Show Surveys" css={{padding: '1em'}}/>
        </div>
            <div style={{display: 'flex'}}>
                {showUsers && (
                    <div style={{flex: 1, padding: '0.5em'}}>
                        <h3>Users</h3>
                        <DataTable value={users} selectionMode="single" selection={selectedUser}
                                   onSelectionChange={(e) => handleUserSelect(e)} dataKey="id" tableStyle={{ minWidth: '20rem' }}>
                            <Column field="note" header="Note"></Column>
                        </DataTable>
                    </div>
                )}
                {showProjects && selectedUser && (
                    <div style={{flex: 1, padding: '0.5em'}}>
                        <h3>Projects</h3>
                        <DataTable value={projects} selectionMode="single" selection={selectedProject}
                                   onSelectionChange={(e) => handleProjectSelect(e)} dataKey="id" tableStyle={{ minWidth: '20rem' }}>
                            <Column field="name" header="Name"></Column>
                        </DataTable>
                    </div>
                )}
                {showSurveys && selectedProject && (
                    <div style={{flex: 1, padding: '0.5em'}}>
                        <h3>Surveys</h3>
                        <DataTable value={surveys} selectionMode="single" selection={selectedSurvey}
                                   onSelectionChange={(e) => setSelectedSurvey(e.value.id.toString())} dataKey="id" tableStyle={{ minWidth: '20rem' }}>
                            <Column field="companyName" header="Company Name"></Column>
                        </DataTable>
                    </div>
                )}
            </div>
        </div>
    );
};