import {Radio} from "antd"
import {Controller, UseFormReturn} from "react-hook-form"
import {KleinverbruikOrGrootverbruik} from 'zero-zummon'

export enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const KleinverbruikGrootverbruikRadios = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    <Radio value={ConnectionType.GROOTVERBRUIK} css={{display: 'block'}}>Grootverbruik</Radio>
                    <Radio value={ConnectionType.KLEINVERBRUIK} css={{display: 'block'}}>Kleinverbruik</Radio>
                </Radio.Group>
            )}
        />
    )
}
