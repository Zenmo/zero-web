import {useLoaderData} from "react-router-dom";
import {useState} from "react";
import {com} from "zero-zummon"
import {useOnce} from "../../hooks/use-once";
import {Survey} from "./survey";
import {getProjectConfiguration} from "./project";

type Survey = com.zenmo.zummon.companysurvey.Survey
const surveyFromJson = com.zenmo.zummon.companysurvey.surveyFromJson

export type SurveyByIdRouteData = {
    surveyId: string,
    deeplink: string | null,
    secret: string | null,
}

export const SurveyById = () => {
    const routeData = useLoaderData() as SurveyByIdRouteData

    const {loading, survey} = useSurvey(routeData)

    if (loading) {
        return <div>Wacht op data...</div>
    }

    if (!survey) {
        return <div>Geen data</div>
    }

    return <Survey survey={survey} project={getProjectConfiguration(survey.zenmoProject)}/>
}



const useSurvey = (routeData: SurveyByIdRouteData): { loading: boolean, survey: any } => {
    const [loading, setLoading] = useState(true)
    const [survey, setSurvey] = useState()

    useOnce(async () => {
        try {
            const url = new URL(process.env.ZTOR_URL + '/company-survey/' + routeData.surveyId)
            if (routeData.deeplink && routeData.secret) {
                url.searchParams.append('deeplink', routeData.deeplink)
                url.searchParams.append('secret', routeData.secret)
            }

            const response = await fetch(url, {
                credentials: 'include',
            })
            if (response.status === 401) {
                window.location.href = process.env.ZTOR_URL + '/login?redirectUrl=' + encodeURIComponent(window.location.href)
                return
            }

            if (response.status === 200) {
                setSurvey(await response.json())
            } else {
                alert((await response.json()).error.message)
            }
        } catch (error) {
            alert((error as Error).message)
        } finally {
            setLoading(false)
        }
    })

    return {
        loading,
        survey
    }
}