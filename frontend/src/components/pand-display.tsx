import React, {createElement, Fragment, FunctionComponent, PropsWithChildren, ReactElement} from 'react'
import {Bag3dProperties} from '../services/3dbag_old'
import {KleinVerbruikPerPostcode, PandData} from '../services/appState'
import {Verblijfsobject} from '../services/bag-verblijfsobject'
import {Bag2DPandProperties} from '../services/bag2d'

export const PandDataDisplay = ({pandData}: { pandData: PandData }) => (
    <div>
        <h2>Pand</h2>
        {pandData.bag2dPand &&
            <PropertyList key="2d" props={pandData.bag2dPand.properties}
                          specs={bag2dDisplaySpec}
                          defaultSpec={bagDefaultDisplaySpec}/>}
        {pandData.bag3dPand &&
            <PropertyList key="3d" props={pandData.bag3dPand.properties}
                          specs={bag3dDisplaySpec}
                          defaultSpec={bag3dDefaultDisplaySpec}/>}
        {createElement(Fragment, {}, ...KleinverbruikDisplay({kleinverbruik: pandData.kleinverbruik}))}

        <h2>Verblijfsobjecten
            ({pandData.bag2dPand?.properties.aantal_verblijfsobjecten})</h2>
        {pandData.verblijfsobjecten.map((verblijfsobject, index) => (
            <>
                <h3>Verblijfsobject {verblijfsobjectLabel(verblijfsobject)}</h3>
                <PropertyList props={verblijfsobject}
                              specs={verblijfsobjectDisplaySpec}
                              defaultSpec={bagDefaultDisplaySpec}/>
            </>
        ))}


        {/*<pre>*/}
        {/*    {JSON.stringify(pandData, undefined, 4)}*/}
        {/*</pre>*/}
    </div>
)

const KleinverbruikDisplay = ({kleinverbruik}: {
    kleinverbruik: { [postcode: string]: KleinVerbruikPerPostcode }
}): ReactElement[] => {
    let result: ReactElement[] = [
        <h2>Gemiddeld jaarverbruik per aansluiting</h2>,
    ]

    const values = Object.values(kleinverbruik)
    if (values.length === 0) {
        return [
            ...result,
            <>onbekend</>,
        ]
    }

    if (values.length === 1) {
        const gas = values[0].gas
        if (gas) {
            result = [
                ...result,
                <DtBold>Gasverbruik</DtBold>,
                <dd>{gas.gemiddeldeStandaardjaarafname} m3</dd>,
                // <DtBold>Aansluiting</DtBold>,
                // <dd>{gas.soortAansluiting} ({gas.soortAansluitingPercentage}%)</dd>,
            ]
        }

        const elektriciteit = values[0].elektriciteit
        if (elektriciteit) {
            result = [
                ...result,
                <DtBold>Electriciteitsverbruik</DtBold>,
                <dd>{elektriciteit.gemiddeldeStandaardjaarafname} kWh</dd>,
                <DtBold>Aansluiting</DtBold>,
                <dd>{elektriciteit.soortAansluiting} ({elektriciteit.soortAansluitingPercentage}%)</dd>,
                <DtBold>Percentage met netto teruglevering</DtBold>,
                <dd>{100 - elektriciteit.leveringsRichtingPercentage}%</dd>,
            ]
        }
    } else {
        return [
            ...result,
            <>TODO: meerdere postcodes in 1 pand</>,
        ]
    }

    return result
}

const verblijfsobjectLabel = (verblijfsObject: Verblijfsobject): string => {
    let result = `${verblijfsObject.openbare_ruimte} ${verblijfsObject.huisnummer}`

    if (verblijfsObject.toevoeging) {
        result += `-${verblijfsObject.toevoeging}`
    }

    if (verblijfsObject.huisletter) {
        result += ` ${verblijfsObject.huisletter}`
    }

    return result
}

const PropertyList = ({props, specs, defaultSpec}: {
    props: object,
    specs: DisplaySpecMap,
    defaultSpec: DisplaySpec
}) => {
    return (
        <>
            {Object.keys(props).map((key) => (
                <ObjectProperty key={key} object={props} objectKey={key}
                                specs={specs} defaultSpec={defaultSpec}/>
            ))}
        </>
    )
}

const ObjectProperty = ({object, objectKey, specs, defaultSpec}: {
    object: { [key: string]: any },
    objectKey: string,
    specs: DisplaySpecMap,
    defaultSpec: DisplaySpec
}) => {
    const spec: DisplaySpec = {
        ...defaultSpec,
        ...specs[objectKey] ?? {},
    }

    if (!spec.visible) {
        return null
    }

    let value = object[objectKey]
    if (typeof value === 'undefined' || value === '') {
        return null
    }

    return <Property key={objectKey} objectKey={objectKey} value={value}
                     spec={spec}/>
}

const Property = ({objectKey, value, spec}: {
    objectKey: string,
    value: any,
    spec: DisplaySpec
}) => {
    if (spec.is_meters) {
        value = Math.round(value * 10) / 10
        value = `${value} m`
    }

    if (spec.is_m2) {
        value = Math.round(value)
        value = `${value} m²`
    }

    if (spec.is_url) {
        value = value.replace('http://', 'https://')
        value = <a href={value}>{value}</a>
    }

    if (typeof value === 'boolean') {
        value = value ? 'Ja' : 'Nee'
    }

    let label = toLabel(objectKey)
    if (spec.label) {
        label = spec.label
    }

    const values: any[] = Array.isArray(value) ? value : [value]

    return (
        <>
            <DtBold>{label}</DtBold>
            {values.map((v: any, i: number) => <dd key={i}>{v}</dd>)}
        </>
    )
}

type DisplaySpecMap = { [key: string]: Partial<DisplaySpec> }

type DisplaySpec = {
    visible: boolean,
    is_url: boolean,
    label: string,
    is_meters: boolean,
    is_m2: boolean,
}

const bagDefaultDisplaySpec: DisplaySpec = {
    visible: true,
    is_url: false,
    label: '',
    is_meters: false,
    is_m2: false,
}

const bag3dDefaultDisplaySpec: DisplaySpec = {
    visible: false, // too many irrelevant props
    is_url: false,
    label: '',
    is_meters: false,
    is_m2: false,
}

const bag2dDisplaySpec: Partial<{ [key in keyof Bag2DPandProperties]: Partial<DisplaySpec> }> = {
    rdf_seealso: {
        is_url: true,
    },
    oppervlakte_min: {
        is_m2: true,
    },
    oppervlakte_max: {
        is_m2: true,
    },
}

const verblijfsobjectDisplaySpec: Partial<{ [key in keyof Verblijfsobject]: Partial<DisplaySpec> }> = {
    rdf_seealso: {
        is_url: true,
    },
    oppervlakte: {
        is_m2: true,
    },
    pandidentificatie: {
        visible: false,
    },
    pandstatus: {
        visible: false,
    },
    bouwjaar: {
        visible: false,
    },
}

const bag3dDisplaySpec: Partial<{ [key in keyof Bag3dProperties]: Partial<DisplaySpec> }> = {
    h_maaiveld: {
        visible: true,
        label: 'Hoogte maaiveld',
        is_meters: true,
    },
    h_dak_50p: {
        visible: true,
        label: 'Mediaan dakhoogte',
        is_meters: true,
    },
    h_dak_max: {
        visible: true,
        label: 'Hoogte nok',
        is_meters: true,
    },
}

export const DtBold: FunctionComponent<PropsWithChildren> = ({children}) => <dt
    style={{fontWeight: 'bold'}}>{children}</dt>

const toLabel = (key: string): string =>
    uppercaseFirst(key).replaceAll('_', ' ')

const uppercaseFirst = (str: string): string =>
    str.charAt(0).toUpperCase() + str.slice(1)
