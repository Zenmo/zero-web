import {useState} from "react";
import {useOnce} from "../hooks/use-once";
import {com} from "zero-zummon"

type Survey = com.zenmo.zummon.companysurvey.Survey
const surveysFromJson = com.zenmo.zummon.companysurvey.surveysFromJson

type UseSurveyReturn = {
    loading: boolean,
    surveys: com.zenmo.zummon.companysurvey.Survey[],
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

    return {
        loading,
        surveys,
    }
}
