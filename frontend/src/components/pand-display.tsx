import {PandData} from "../services/appState";
import {Bag2DPandProperties} from "../services/bag2d";
import {FunctionComponent, PropsWithChildren} from "react";
import {Bag3dProperties} from "../services/3dbag_old";
import {Bag2DVerblijfsobject, VerblijfsobjectProperties} from "../services/bag-verblijfsobject";


export const PandDataDisplay = ({pandData}: {pandData: PandData}) => (
    <div>
        <h2>Pand</h2>
        {pandData.bag2dPand && <PropertyList props={pandData.bag2dPand.properties} specs={bag2dDisplaySpec} defaultSpec={bagDefaultDisplaySpec}/>}
        {pandData.bag3dPand && <PropertyList props={pandData.bag3dPand.properties} specs={bag3dDisplaySpec} defaultSpec={bag3dDefaultDisplaySpec}/>}
        <h2>{pandData.bag2dPand?.properties.aantal_verblijfsobjecten} Verblijfsobjecten</h2>
        {pandData.verblijfsobjecten.map((verblijfsobject, index) => (
            <>
                <h3>Verblijfsobject {verblijfsobjectLabel(verblijfsobject.properties)}</h3>
                <PropertyList props={verblijfsobject.properties} specs={verblijfsobjectDisplaySpec} defaultSpec={bagDefaultDisplaySpec}/>
            </>
        ))}

        {/*<pre>*/}
        {/*    {JSON.stringify(pandData, undefined, 4)}*/}
        {/*</pre>*/}
    </div>
)

const verblijfsobjectLabel = (verblijfsObject: VerblijfsobjectProperties): string => {
    let result = `${verblijfsObject.openbare_ruimte} ${verblijfsObject.huisnummer}`

    if (verblijfsObject.toevoeging) {
        result += `-${verblijfsObject.toevoeging}`
    }

    if (verblijfsObject.huisletter) {
        result += ` ${verblijfsObject.huisletter}`
    }

    return result
}

const PropertyList = ({props, specs, defaultSpec}: {props: object, specs: DisplaySpecMap, defaultSpec: DisplaySpec}) => {
    return (
        <>
            {Object.keys(props).map((key) => (
                <ObjectProperty key={key} object={props} objectKey={key} specs={specs} defaultSpec={defaultSpec} />
            ))}
        </>
    )
}


const ObjectProperty = ({object, objectKey, specs, defaultSpec}: {
    object: {[key: string]: any},
    objectKey: string,
    specs: DisplaySpecMap,
    defaultSpec: DisplaySpec
}) => {
    let spec = specs[objectKey] ?? {}
    spec = {
        ...defaultSpec,
        ...spec,
    }

    if (spec.visible === false) {
        return null
    }

    let value = object[objectKey]
    if (typeof value === "undefined" || value === "") {
        return null
    }

    // @ts-ignore
    return <Property key={objectKey} objectKey={objectKey} value={value} spec={spec}/>
}

const Property = ({objectKey, value, spec}: { objectKey: string, value: any, spec: DisplaySpec }) => {
    if (spec.is_meters) {
        value = Math.round(value*10)/10
        value = `${value} m`
    }

    if (spec.is_m2) {
        value = Math.round(value)
        value = `${value} m²`
    }

    if (spec.is_url) {
        value = <a href={value}>{value}</a>
    }

    if (typeof value === "boolean") {
        value = value ? "Ja" : "Nee"
    }

    let label = toLabel(objectKey)
    if (spec.label) {
        label = spec.label
    }

    return (
        <>
            <DtBold>{label}</DtBold>
            <dd>{value}</dd>
        </>
    )
}

type DisplaySpecMap = {[key: string]: Partial<DisplaySpec>}

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
    label: "",
    is_meters: false,
    is_m2: false,
}

const bag3dDefaultDisplaySpec: DisplaySpec = {
    visible: false, // too many irrelevant props
    is_url: false,
    label: "",
    is_meters: false,
    is_m2: false,
}

const bag2dDisplaySpec: Partial<{[key in keyof Bag2DPandProperties]: Partial<DisplaySpec>}> = {
    rdf_seealso: {
        is_url: true,
    },
}

const verblijfsobjectDisplaySpec: Partial<{[key in keyof VerblijfsobjectProperties]: Partial<DisplaySpec>}> = {
    rdf_seealso: {
        is_url: true,
    },
    oppervlakte: {
        is_m2: true,
    },
    pandidentificatie: {
        visible: false
    },
    pandstatus: {
        visible: false
    },
    bouwjaar: {
        visible: false
    }
}

const bag3dDisplaySpec: Partial<{[key in keyof Bag3dProperties]: Partial<DisplaySpec>}> = {

}


export const DtBold: FunctionComponent<PropsWithChildren> = ({children}) => <dt style={{fontWeight: "bold"}}>{children}</dt>

const toLabel = (key: string): string =>
    uppercaseFirst(key).replaceAll("_", " ")

const uppercaseFirst = (str: string): string =>
    str.charAt(0).toUpperCase() + str.slice(1);
