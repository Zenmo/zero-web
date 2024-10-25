import {FunctionComponent} from "react"
import {Survey} from "./survey"
import { useParams } from "react-router-dom"
import {getProjectConfiguration} from "./project"
import {assertDefined} from "../../services/util"

export const NewSurveyByProjectName: FunctionComponent<{}> = () => (
    <Survey project={getProjectConfiguration(assertDefined(useParams().projectName))} />
)
