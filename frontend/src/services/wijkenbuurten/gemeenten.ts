export async function fetchGemeenteList(): Promise<string[]> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'gemeenten',
        service: 'WFS',
        version: '2.0.0',
        propertyName: 'gemeentenaam',
        // We're using XML output here because it has the side-effect of not returning the geometry.
        // This makes the response much smaller.
        //outputFormat: 'json',
        filter: `
            <Filter>
                <PropertyIsEqualTo>
                    <PropertyName>water</PropertyName>
                    <Literal>NEE</Literal>
                </PropertyIsEqualTo>
            </Filter>
        `,
    })

    const url = 'https://service.pdok.nl/cbs/wijkenbuurten/2022/wfs/v1_0?' + params.toString()
    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting gemeente list')
    }

    const body = await response.text()
    const document = new DOMParser().parseFromString(body, 'text/xml')
    const elements = document.getElementsByTagName('wijkenbuurten:gemeentenaam')
    const gemeenten: string[] = Array.prototype.map.call(elements, (element: Element) => {
        return element.textContent
    }) as string[]

    return gemeenten.sort()
}