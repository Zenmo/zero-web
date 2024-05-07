import {useLocation, Location} from "react-router-dom"

export const ThankYou = () => {
    let location = useLocation()
    const deeplinkUrl = buildDeeplinkUrl(location)

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

const buildDeeplinkUrl = (location: Location): string | null => {
    const deeplink = location.state?.deeplink
    if (!deeplink) {
        return null
    }

    const url = new URL(window.location.origin)
    url.pathname = '/bedrijven-uitvraag/' + deeplink.surveyId
    url.searchParams.append('deeplink', deeplink.deeplinkId)
    url.searchParams.append('secret', deeplink.secret)

    return url.toString()
}
