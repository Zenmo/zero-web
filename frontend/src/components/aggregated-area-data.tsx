import {AppState} from "../services/appState";
import {createElement as h} from "react";
import {DtBold} from "./pand-display";
import {Bag2DVerblijfsobject} from "../services/bag-verblijfsobject";
import {Bag2DPand} from "../services/bag2d";


export const AggregatedAreaData = ({appState}: {appState: AppState}) => (
    h('dl', {},
        h(DtBold, {}, "Aantal panden"),
        h('dd', {}, appState.bag2dPanden.length),
        h(DtBold, {}, "Gemiddeld bouwjaar"),
        h('dd', {}, averageBouwjaar(appState.bag2dPanden)),
        h(DtBold, {}, "Aantal verblijfsobjecten"),
        h('dd', {}, appState.verblijfsobjecten.length),
        h(DtBold, {}, "Vloeroppervlak"),
        // TODO: does not include panden with no verblijfsobject, like factory floors
        h('dd', {}, sumVloeroppervlak(appState.verblijfsobjecten).toLocaleString('nl-NL') + " m²"),
        h(DtBold, {}, "Gebruiksdoelen"),
        h('dd', {}, Object.entries(gebruiksdoelen(appState.verblijfsobjecten))
                .map(([gebruiksdoel, aantal]) => h('div', {key: gebruiksdoel}, `${gebruiksdoel} (${aantal}x)`))
        )
    )
)

const sumVloeroppervlak = (verblijfsobjecten: Bag2DVerblijfsobject[]): number =>
    verblijfsobjecten
        .map(verblijfsobject => verblijfsobject.properties.oppervlakte)
        .reduce((acc, val) => acc + val, 0)

const averageBouwjaar = (panden: Bag2DPand[]) => {
    const bouwjaren = panden
        .map(pand => pand.properties.bouwjaar)
        .filter(bouwjaar => bouwjaar)

    if (bouwjaren.length === 0) {
        return 1995
    }

    const sum = bouwjaren.reduce((sum, bouwjaar) => sum + bouwjaar, 0)

    const average = sum / bouwjaren.length

    return Math.round(average)
}

const gebruiksdoelen = (verblijfsobjecten: Bag2DVerblijfsobject[]): {[gebruiksdoel: string]: number} =>
    verblijfsobjecten
        // TODO: split comma-separated values
        .map(verblijfsobject => verblijfsobject.properties.gebruiksdoel)
        .reduce((acc, gebruiksdoel) => ({
            ...acc,
            // @ts-ignore
            [gebruiksdoel]: (acc[gebruiksdoel] ?? 0) + 1,
        }), {})
