import React, {FunctionComponent} from "react";
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {useUsers} from "./use-users";
import {PrimeReactProvider} from "primereact/api";
import {User} from "zero-zummon"

import "primereact/resources/themes/lara-light-cyan/theme.css"
import 'primeicons/primeicons.css'
import {DeleteButton} from "./delete-button";
import {EditButton} from "./edit-button";
import {Button} from "primereact/button";
import {useNavigate} from "react-router-dom"

export const Users: FunctionComponent = () => {
    const {loadingUsers, users, removeUser} = useUsers()
    const navigate = useNavigate();

    return (
        <PrimeReactProvider>
            <div css={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '1em 1em',
                boxShadow: '1px solid #ddd'
            }}>
                <h3>Users List</h3>
                <Button
                    label="Nieuw"
                    icon="pi pi-pencil"
                    onClick={(event) => navigate(`/users/new-user`)}
                />
            </div>
            <DataTable
                value={users}
                loading={loadingUsers}
                sortField="created"
                sortOrder={-1}
                filterDisplay="row"
            >
                {/*<Column field="id" header="keycloak Id" sortable filter/>*/}
                <Column field="note" header="Note" sortable filter/>

                <Column body={(user: User) => (
                    <div css={{
                        display: 'flex',
                        '> *': {
                            margin: `${1 / 6}rem`
                        },
                    }}>
                        <DeleteButton type="users" id={user.id} onDelete={removeUser}/>
                        <EditButton type="users" id={user.id}/>
                    </div>
                )}/>
            </DataTable>
        </PrimeReactProvider>
    )
}