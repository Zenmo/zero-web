import {Point, Position} from 'geojson'
import {LatLngBounds} from 'leaflet'
import unionBy from 'lodash/unionBy'
import {BoundingBox} from './bag2d'

// https://imbag.github.io/praktijkhandleiding/artikelen/welk-gebruiksdoel-moet-worden-geregistreerd
export enum GebruiksDoel {
    // Gebruiksfunctie voor het wonen
    woonfunctie = 'woonfunctie',
    //Gebruiksfunctie voor het samenkomen van personen voor kunst, cultuur, godsdienst, communicatie, kinderopvang, het verstrekken van consumpties voor het gebruik ter plaatse of het aanschouwen van sport
    bijeenkomstfunctie = 'bijeenkomstfunctie',
    // Gebruiksfunctie voor het dwangverblijf van personen
    celfunctie = 'celfunctie',
    // Gebruiksfunctie voor medisch onderzoek, verpleging, verzorging of behandeling
    gezondheidszorgfunctie = 'gezondheidszorgfunctie',
    // Gebruiksfunctie voor het bedrijfsmatig bewerken of opslaan van materialen en goederen, of voor agrarische doeleinden
    industriefunctie = 'industriefunctie',
    // Gebruiksfunctie voor administratie
    kantoorfunctie = 'kantoorfunctie',
    // Gebruiksfunctie voor het bieden van recreatief verblijf of tijdelijk onderdak aan personen
    logiesfunctie = 'logiesfunctie',
    // Gebruiksfunctie voor het geven van onderwijs
    onderwijsfunctie = 'onderwijsfunctie',
    // Gebruiksfunctie voor het beoefenen van sport
    sportfunctie = 'sportfunctie',
    // Gebruiksfunctie voor het verhandelen van materialen, goederen of diensten
    winkelfunctie = 'winkelfunctie',
    // Niet in dit lid benoemde gebruiksfunctie voor activiteiten waarbij het verblijven van personen een ondergeschikte rol speelt
    overige_gebruiksfunctie = 'overige gebruiksfunctie',
}

export type Verblijfsobject = {
    feature_id: string // e.g. "verblijfsobject.144b58ad-1e01-4025-b275-db34fc19ebf7"
    position: Position,
    identificatie: string, // example: "0772010000726364"
    pandidentificatie: string, // example: "0772100000306503"
    rdf_seealso: string, // url
    oppervlakte: number,
    status: string,
    gebruiksdoelen: GebruiksDoel[],
    openbare_ruimte: string,
    huisnummer: number,
    huisletter: string,
    toevoeging: string,
    postcode: string, // 1111AA
    woonplaats: string,
    // is this always the same as pand?
    bouwjaar: string,
    pandstatus: string,
}

type VerblijfsobjectFeature = {
    type: 'Feature',
    id: string // e.g. "verblijfsobject.144b58ad-1e01-4025-b275-db34fc19ebf7"
    properties: VerblijfsobjectProperties,
    bbox: BoundingBox,
    geometry: Point,
}

// see https://service.pdok.nl/lv/bag/wfs/v2_0?version=2.0.0&service=WFS&request=DescribeFeatureType&typename=bag:verblijfsobject
type VerblijfsobjectProperties = {
    identificatie: string, // example: "0772010000726364"
    pandidentificatie: string, // example: "0772100000306503"
    rdf_seealso: string, // url
    oppervlakte: number,
    status: string,
    gebruiksdoel: string,
    openbare_ruimte: string, // street name
    huisnummer: number,
    huisletter: string,
    toevoeging: string,
    postcode: string, // 1111AA
    woonplaats: string,
    bouwjaar: string,
    pandstatus: string,
}

export async function getBagVerblijfsobjecten(boundingBox: LatLngBounds, startIndex = 0): Promise<Verblijfsobject[]> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        service: 'WFS',
        typeName: 'bag:verblijfsobject',
        outputFormat: 'json',
        srsName: 'EPSG:4326', // GPS
        bbox: [
            boundingBox.getWest(),
            boundingBox.getSouth(),
            boundingBox.getEast(),
            boundingBox.getNorth(),
            'urn:ogc:def:rs:EPSG::4326', // input coordinate system
        ].join(','),
        version: '2.0.0',
        startIndex: startIndex.toString(),
    })

    const url = 'https://service.pdok.nl/lv/bag/wfs/v2_0?' + params.toString()

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting BAG Verblijfsobjecten')
    }

    const body = await response.json() as any

    // There seems no consistent indication of whether there are more results.
    // We assume this is the last page if there are less than 900 results.
    if (body.features.length < 900) {
        return body.features.map(postProcessing)
    }

    // Recursively fetch the next page.
    // There is sometimes overlap between the last items of the previous page
    // and the first items of the next page.
    // We remove these duplicates.
    // (it is kinda inefficient to de-duplicate in every recursive call)
    return unionBy(
        body.features.map(postProcessing),
        await getBagVerblijfsobjecten(boundingBox, startIndex + body.features.length),
        'feature_id',
    )
}

// Create a flatter, more strongly typed object
const postProcessing = (verblijfsobject: VerblijfsobjectFeature): Verblijfsobject => {
    // unpack into new object so that any unknown/added properties are preserved
    let result: Verblijfsobject = {
        ...verblijfsobject.properties,
        feature_id: verblijfsobject.id,
        position: verblijfsobject.geometry.coordinates,
        gebruiksdoelen: verblijfsobject.properties.gebruiksdoel.split(',').filter(Boolean) as GebruiksDoel[],
    }

    // @ts-ignore
    delete result.gebruiksdoel

    return result
}
