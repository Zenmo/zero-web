import {FunctionComponent, useState} from "react"
import {ztorFetch} from "../services/ztor-fetch"
import {BooleanCheckbox} from "./boolean-checkbox"

export const SurveyActiveCheckbox: FunctionComponent<{
    surveyId: any,
    active: boolean,
    setActive: (active: boolean) => void
}> = (
    {surveyId, active, setActive}
) => {
    const [disabled, setDisabled] = useState(false);
    const onChange = async (active: boolean) => {
        setDisabled(true);
        try {
            await ztorFetch(`/company-surveys/${surveyId}/active`, {
                method: 'PUT',
                body: JSON.stringify(active),
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            setActive(active)
        } catch (e) {
            alert(`Error setting active state: ${e}`)
        } finally {
            setDisabled(false);
        }
    }

    return <BooleanCheckbox
        value={active}
        onChange={onChange}
        disabled={disabled}
    />
}
