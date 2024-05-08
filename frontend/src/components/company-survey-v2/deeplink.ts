
export const buildDeeplinkUrl = (deeplink: any): string => {
    const url = new URL(window.location.origin)
    url.pathname = '/bedrijven-uitvraag/' + deeplink.surveyId
    url.searchParams.append('deeplink', deeplink.deeplinkId)
    url.searchParams.append('secret', deeplink.secret)

    return url.toString()
}
