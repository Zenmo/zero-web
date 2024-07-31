import {useState} from "react";
import {useOnce} from "../hooks/use-once";
import {Survey, surveysFromJson} from "zero-zummon"

type UseSurveyReturn = {
    loading: boolean,
    surveys: Survey[],
    removeSurvey: (surveyId: string) => void,
}

export const useSurveys = (): UseSurveyReturn => {
    const [loading, setLoading] = useState(true)
    const [surveys, setSurveys] = useState<Survey[]>([])

    useOnce(async () => {
        try {
            const response = await fetch(process.env.ZTOR_URL + '/company-survey', {
                credentials: 'include',
            })
            if (response.status === 401) {
                window.location.href = process.env.ZTOR_URL + '/login?redirectUrl=' + encodeURIComponent(window.location.href)
                return
            }

            setSurveys(surveysFromJson(await response.text()))
        } catch (error) {
            alert((error as Error).message)
        } finally {
            setLoading(false)
        }
    })

    const removeSurvey = (surveyId: any) => {
        setSurveys(surveys.filter(survey => survey.id.toString() !== surveyId.toString()))
    }

    return {
        loading,
        surveys,
        removeSurvey,
    }
}
