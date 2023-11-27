import {Radio} from 'antd'
import {Controller, UseFormReturn} from 'react-hook-form'

enum ConsumptionProfile {
    // TODO add images and better names
    ONE = "ONE",
    TWO = "TWO",
    THREE = "THREE",
    FOUR = "FOUR",
}

export const ConsumptionProfileSelect = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    {Object.values(ConsumptionProfile).map(capacity => (
                        <Radio key={capacity} value={capacity} css={{display: 'block'}}>{capacity}</Radio>
                    ))}
                </Radio.Group>
            )}
        />
    )
}