import {FunctionComponent} from "react";
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {useSurveys} from "./use-surveys";
import {PrimeReactProvider} from "primereact/api";
import { Button } from 'primereact/button';

import "primereact/resources/themes/lara-light-cyan/theme.css";

export const Admin: FunctionComponent = () => {
    const {loading, surveys} = useSurveys()

    return (
        <PrimeReactProvider>
            <div>
                <h1>Uitvraag bedrijven</h1>
                <DataTable
                    value={surveys}
                    loading={loading}
                    sortField="created"
                    sortOrder={-1}
                    filterDisplay="row"
                >
                    <Column field="zenmoProject" header="Project" sortable filter />
                    <Column field="companyName" header="Bedrijf" sortable filter />
                    <Column field="personName" header="Contactpersoon" sortable filter />
                    <Column field="email" header="E-mail" sortable filter />
                    <Column header="Aansluitingen" body={survey => (
                        survey.addresses.reduce((acc: number, address: any) => acc + address.gridConnections.length, 0).toString()
                    )} />
                    {/* TODO: bestanden */}
                    <Column field="created" header="Opgestuurd op" sortable />
                    {/*<Column body={() => <Button />}/>*/}
                </DataTable>
            </div>
        </PrimeReactProvider>
    )
}
