import {useLocation, Location} from "react-router"
import {buildDeeplinkUrl} from "./company-survey-v2/deeplink"

export const ThankYou = () => {
    let location = useLocation()
    const deeplink = location.state?.deeplink
    let deeplinkUrl = null
    if (deeplink) {
        deeplinkUrl = buildDeeplinkUrl(deeplink)
    }


    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            maxWidth: '40rem',
            alignItems: 'center',
            textAlign: "center",
        }}>
            <h1>Dank u voor het invullen!</h1>
            {deeplinkUrl && (
                <>
                    <p>U kunt uw gegevens terugkijken en bewerken op</p>
                    <a href={deeplinkUrl}>{deeplinkUrl}</a>
                </>
            )}
        </div>
    )
}
