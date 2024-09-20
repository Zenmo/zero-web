import {FunctionComponent, useState} from "react"
import {Steps} from "primereact/steps"
import {ZeroLayout} from "../components/zero-layout"
import {PrimeReactProvider} from "primereact/api"
import {ExcelUpload} from "./excel-upload"
import {SurveyWithErrors} from "zero-zummon"
import {Feedback} from "./feedback"
import {PandenSelectLoader} from "./panden-select-loader"

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
                <Steps
                    style={{
                        margin: "1rem",
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
                    }, {
                        label: "Opslaan",
                    }]
                }/>
                {activeIndex === 0 && <ExcelUpload setSurveyWithErrors={setSurveyWithErrors}/>}
                {activeIndex === 1 && surveyWithErrors && <Feedback navigateNext={() => setActiveIndex(2)} surveyWithErrors={surveyWithErrors}/>}
                {activeIndex === 2 && <PandenSelectLoader project={surveyWithErrors?.survey.project}/>}
                {activeIndex === 3 && <div>Opslaan</div>}
            </ZeroLayout>
        </PrimeReactProvider>
    )
}