import {FunctionComponent, useState} from "react"
import {Steps} from "primereact/steps"
import {ZeroLayout} from "../components/zero-layout"
import {PrimeReactProvider} from "primereact/api"
import {ExcelUpload} from "./excel-upload"
import {SurveyWithErrors} from "zero-zummon"
import {Feedback} from "./feedback"

export const ExcelImport: FunctionComponent = () => {
    const [activeIndex, setActiveIndex] = useState(0);
    const [surveyWithErrors, setSurveyWithErrorsState] = useState<SurveyWithErrors | null>(null)
    const setSurveyWithErrors = (swe: SurveyWithErrors) => {
        setActiveIndex(1)
        setSurveyWithErrorsState(swe)
    }

    return (
        <PrimeReactProvider>
            <ZeroLayout subtitle="Importeer Excel">
                <div css={{margin: "1rem"}}>
                    <Steps
                        style={{
                            marginBottom: "1rem",
                        }}
                        activeIndex={activeIndex}
                        onSelect={(e) => setActiveIndex(e.index)}
                        readOnly={false}
                        model={[{
                            label: "Uploaden",
                        }, {
                            label: "Feedback",
                        }, {
                            label: "Panden selecteren",
                        }]
                    }/>
                    {activeIndex === 0 && <ExcelUpload setSurveyWithErrors={setSurveyWithErrors}/>}
                    {activeIndex === 1 && surveyWithErrors && <Feedback surveyWithErrors={surveyWithErrors}/>}
                    {activeIndex === 2 && <div>Panden selecteren</div>}
                </div>
            </ZeroLayout>
        </PrimeReactProvider>
    )
}