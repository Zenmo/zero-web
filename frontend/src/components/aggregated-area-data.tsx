import {createElement as h} from 'react'
import {AppHook} from '../services/appState'
import {Verblijfsobject} from '../services/bag-verblijfsobject'
import {Bag2DPand} from '../services/bag2d'
import {count, empty, filter, flatMap, map, reduce, toIterable} from '../services/iterable'
import {DtBold} from './pand-display'

export const AggregatedAreaData = ({appHook: {bag2dPanden, verblijfsobjecten}}: { appHook: AppHook }) => (
    h('dl', {},
        h(DtBold, {}, 'Aantal panden'),
        h('dd', {}, count(bag2dPanden)),
        h(DtBold, {}, 'Gemiddeld bouwjaar'),
        h('dd', {}, averageBouwjaar(bag2dPanden)),
        h(DtBold, {}, 'Aantal verblijfsobjecten'),
        h('dd', {}, count(verblijfsobjecten)),
        h(DtBold, {}, 'Vloeroppervlak'),
        // TODO: does not include panden with no verblijfsobject, like factory floors
        h('dd', {}, sumVloeroppervlak(verblijfsobjecten).toLocaleString('nl-NL') + ' m²'),
        h(DtBold, {}, 'Gebruiksdoelen'),
        h('dd', {}, [...map(
            gebruiksdoelenOverzicht(verblijfsobjecten).entries(),
            ([gebruiksdoel, aantal]) => h('div', {key: gebruiksdoel}, `${gebruiksdoel} (${aantal}x)`)),
        ]),
    )
)

const sumVloeroppervlak = (verblijfsobjecten: Iterable<Verblijfsobject>): number =>
    reduce(
        map(verblijfsobjecten, verblijfsobject => verblijfsobject.oppervlakte),
        (acc, val) => acc + val,
        0,
    )

const averageBouwjaar = (panden: Iterable<Bag2DPand>) => {
    const bouwjaren = toIterable(() => filter(
        map(panden, pand => pand.properties.bouwjaar),
        bouwjaar => Boolean(bouwjaar),
    ))

    if (empty(bouwjaren)) {
        return 1995
    }

    const sum = reduce(bouwjaren, (acc, val) => acc + val, 0)

    const average = sum / count(bouwjaren)

    return Math.round(average)
}

const gebruiksdoelenOverzicht = (verblijfsobjecten: Iterable<Verblijfsobject>): Map<string, number> =>
    reduce(
        flatMap(verblijfsobjecten, verblijfsobject => verblijfsobject.gebruiksdoelen),
        (acc, gebruiksdoel) => acc.set(gebruiksdoel, (acc.get(gebruiksdoel) ?? 0) + 1),
        new Map<string, number>(),
    )
