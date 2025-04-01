import {Button} from "primereact/button";
import {FunctionComponent, useState} from "react";
import {noop} from "lodash";
import {ZTOR_BASE_URL} from "../services/ztor-fetch";

export const DeleteButton: FunctionComponent<{type: string, id: any, onDelete?: (type: string , id: any) => void}> = ({type, id, onDelete = noop}) => {
    const [pending, setPending] = useState(false)
    const deleteSurvey = async () => {
        setPending(true)
        try {
            if (!confirm('Uitvraag verwijderen?')) {
                return
            }
            const response = await fetch(`${ZTOR_BASE_URL}/${type}/${id}`, {
                method: 'DELETE',
                credentials: 'include',
            })
            if (!response.ok) {
                throw new Error(`Could not delete: ${response.status} ${response.statusText}`)
            }
            onDelete(id)
        } catch (error) {
            alert((error as Error).message)
        } finally {
            setPending(false)
        }
    }

    return (
        <Button className={'btn btn-danger btn-icon btn-sm'} icon="pi pi-trash" loading={pending} onClick={deleteSurvey} severity="danger" aria-label="Verwijderen" />
    )
}


type DeleteSurveyProps = {
    type: string,
    id: any,
    onDelete: (id: any) => void,
    setPending: (pending: boolean) => void
}

export const deleteSurvey = async ({type, id, onDelete, setPending}: DeleteSurveyProps) => {
    setPending(true);
    try {
        if (!confirm('Uitvraag verwijderen?')) {
            return;
        }
        const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/${type}/${id}`, {
            method: 'DELETE',
            credentials: 'include',
        });
        if (!response.ok) {
            throw new Error(`Could not delete: ${response.status} ${response.statusText}`);
        }
        onDelete(id);
    } catch (error) {
        alert((error as Error).message);
    } finally {
        setPending(false);
    }
};