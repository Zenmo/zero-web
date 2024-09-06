import {FunctionComponent, useState} from "react"
import {SurveyWithErrors} from "zero-zummon"
import {Message} from "primereact/message"
import {Button} from "primereact/button"
import {mapOrElse} from "../services/util"
import {useToggle} from "../hooks/use-once"

export const Feedback: FunctionComponent<{ surveyWithErrors: SurveyWithErrors }> = ({surveyWithErrors}) => {
    const [dataVisible, toggleDataVisible] = useToggle()

    return (
        <>
            <div style={{
                display: "flex",
                flexDirection: "column",
                gap: "1rem",
                paddingBottom: "1rem",
            }}>
                {mapOrElse(
                    surveyWithErrors.errors.asJsReadonlyArrayView(),
                    error => <Message severity="warn" text={error} />,
                    () => <Message severity="info" text="Alle checks OK" />,
                )}
            </div>
            <div style={{
                display: "flex",
                gap: "1rem",
            }}>
                <Button label={dataVisible ? "{} Data verbergen" : "{} Data bekijken"} onClick={toggleDataVisible}/>
                <Button label="Panden selecteren" icon="pi pi-arrow-right" />
            </div>
            {dataVisible && <pre>{surveyWithErrors.survey.toPrettyJson()}</pre>}
        </>
    )
}

