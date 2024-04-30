import {FunctionComponent} from "react";
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import {useSurveys} from "./use-surveys";
import {PrimeReactProvider} from "primereact/api";
import { Button } from 'primereact/button';
import {com} from "zero-zummon"

import "primereact/resources/themes/lara-light-cyan/theme.css";

type Survey = com.zenmo.zummon.companysurvey.Survey
type Address = com.zenmo.zummon.companysurvey.Address

const formatByteSize = com.zenmo.zummon.companysurvey.formatByteSize

export const Admin: FunctionComponent = () => {
    const {loading, surveys} = useSurveys()
    if (surveys.length > 0) {
        console.log(surveys[0].created)
    }

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
                    <Column header="Aansluitingen" sortable field="numGridConnections" />
                    {/* TODO: bestanden */}
                    <Column header="Bestanden" body={(survey: Survey ) => (
                        <>
                            {survey.filesArray.map(file => (
                                <div key={file.blobName}>
                                    <a href={downloadUrl(file.blobName)}>{file.originalName}</a>
                                    &nbsp;
                                    ({formatByteSize(file.size)})
                                </div>
                            ))}
                        </>
                    )} />
                    <Column field="createdToString" body={survey => formatDatetime(survey.created.toString())} header="Opgestuurd op" sortable />
                    {/*<Column body={() => <Button />}/>*/}
                </DataTable>
            </div>
        </PrimeReactProvider>
    )
}

const downloadUrl = (blobName: string) =>
    process.env.ZTOR_URL + "/download?blobName=" + encodeURIComponent(blobName)

// Doing it in JavaScript because no timezone available in Kotlin.
const formatDatetime = (date: string) => {
    const dateTime = new Date(date)

    return dateTime.getFullYear() + "-" +
        (dateTime.getMonth() + 1).toString().padStart(2, "0") + "-" +
        dateTime.getDate().toString().padStart(2, "0") +
        " " + dateTime.getHours().toString().padStart(2, "0") +
        ":" + dateTime.getMinutes().toString().padStart(2, "0")
}
