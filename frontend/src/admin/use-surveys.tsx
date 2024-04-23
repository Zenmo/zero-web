import {useState} from "react";
import {useOnce} from "../hooks/use-once";

type UseSurveyReturn = {
    loading: boolean,
    surveys: any[],
}

export const useSurveys = (): UseSurveyReturn => {
    const [loading, setLoading] = useState(true)
    const [surveys, setSurveys] = useState([])
    useOnce(async () => {
        try {
            const response = await fetch(process.env.ZTOR_URL + '/company-survey', {
                credentials: 'include',
            })
            if (response.status === 401) {
                window.location.href = process.env.ZTOR_URL + '/login?redirectUrl=' + encodeURIComponent(window.location.href)
                return
            }

            setSurveys(await response.json())
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
