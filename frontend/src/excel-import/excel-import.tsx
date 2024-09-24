import {FunctionComponent, useState} from "react"
import {Steps} from "primereact/steps"
import {ZeroLayout} from "../components/zero-layout"
import {PrimeReactProvider} from "primereact/api"
import {ExcelUpload} from "./excel-upload"
import {SurveyWithErrors, PandID} from "zero-zummon"
import {Feedback} from "./feedback"
import {PandenSelectLoader} from "./panden-select-loader"
import {Save} from "./save"

export const ExcelImport: FunctionComponent = () => {
    const [activeIndex, setActiveIndex] = useState(0);
    const [surveyWithErrors, setSurveyWithErrorsState] = useState<SurveyWithErrors | undefined>(undefined)

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
                {activeIndex === 0 && <ExcelUpload setSurveyWithErrors={(swe) => {
                    setActiveIndex(1)
                    setSurveyWithErrorsState(swe)
                }}/>}
                {activeIndex === 1 && surveyWithErrors &&
                    <Feedback navigateNext={() => setActiveIndex(2)} surveyWithErrors={surveyWithErrors}/>}
                {activeIndex === 2 && surveyWithErrors?.survey &&
                    <PandenSelectLoader
                        project={surveyWithErrors?.survey.project}
                        thisCompanyPandIds={surveyWithErrors?.survey.getSingleGridConnection().pandIds.asJsReadonlySetView()}
                        addThisCompanyPandId={(pandId: PandID) => {
                            setSurveyWithErrorsState(
                                surveyWithErrors.withPandId(pandId)
                            )
                        }}
                        removeThisCompanyPandId={(pandId: PandID) => {
                            setSurveyWithErrorsState(
                                surveyWithErrors.withoutPandId(pandId)
                            )
                        }}/>}
                {activeIndex === 3 && surveyWithErrors && <Save survey={surveyWithErrors.survey} /> }
            </ZeroLayout>
        </PrimeReactProvider>
    )
}