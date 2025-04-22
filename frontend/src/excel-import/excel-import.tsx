import {FunctionComponent, useState} from "react"
import {PandID, SurveyWithErrors} from "zero-zummon"
import {ZeroLayout} from "../components/zero-layout"
import {PrimeReactProvider} from "primereact/api"
import {Steps} from "primereact/steps"
import {Message} from "primereact/message"
import {ExcelUpload} from "./excel-upload"
import {Feedback} from "./feedback"
import {PandenSelectLoader} from "../panden-select/panden-select-loader"
import {Save} from "./save"
import {useOnce} from "../hooks/use-once"
import {setValidationLanguage} from "../services/set-validation-language"
import {Content} from "../components/Content"

export const ExcelImport: FunctionComponent = () => {
    const [activeIndex, setActiveIndex] = useState(0)
    const [surveyWithErrors, setSurveyWithErrorsState] = useState<SurveyWithErrors | undefined>(undefined)

    useOnce(() => {
        setValidationLanguage()
    })

    return (
        <PrimeReactProvider>
            <Content>
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
                            disabled: (surveyWithErrors == undefined),
                        }, {
                            label: "Panden selecteren",
                            disabled: (surveyWithErrors == undefined),
                        }, {
                            label: "Review",
                        }, {
                            label: "Opslaan",
                        }]
                        } />
                    {activeIndex === 0 && <ExcelUpload setSurveyWithErrors={(swe) => {
                        setActiveIndex(1)
                        setSurveyWithErrorsState(swe)
                    }} />}
                    {activeIndex === 1 && surveyWithErrors &&
                        <Feedback navigateNext={() => setActiveIndex(2)} surveyWithErrors={surveyWithErrors} />}
                    {activeIndex === 2 && surveyWithErrors?.survey && surveyWithErrors.survey.project &&
                        <PandenSelectLoader
                            buurtcodes={surveyWithErrors.survey.project.buurtCodes.asJsReadonlyArrayView()}
                            thisCompanyPandIds={surveyWithErrors?.survey.getSingleGridConnection().pandIds.asJsReadonlySetView()}
                            addThisCompanyPandId={(pandId: PandID) => {
                                setSurveyWithErrorsState(
                                    surveyWithErrors.withPandId(pandId),
                                )
                            }}
                            removeThisCompanyPandId={(pandId: PandID) => {
                                setSurveyWithErrorsState(
                                    surveyWithErrors.withoutPandId(pandId),
                                )
                            }} />}
                    {activeIndex === 3 && surveyWithErrors &&
                        <Message style={{margin: "1rem"}} severity="info" text="Alle checks OK" />}
                    {activeIndex === 4 && surveyWithErrors && <Save survey={surveyWithErrors.survey} />}
                </ZeroLayout>
            </Content>
        </PrimeReactProvider>
    )
}
