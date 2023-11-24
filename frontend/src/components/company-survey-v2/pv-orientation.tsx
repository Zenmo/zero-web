import {Radio} from 'antd'
import {Controller, UseFormReturn} from 'react-hook-form'

enum PVOrientationOptions {
    SOUTH = "SOUTH",
    EAST_WEST = "EAST_WEST",
}

export const PVOrientation = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    <Radio value={PVOrientationOptions.SOUTH} css={{display: 'block'}}>Zuid</Radio>
                    <Radio value={PVOrientationOptions.EAST_WEST} css={{display: 'block'}}>Oost-west</Radio>
                    <Radio value={undefined} css={{display: 'block'}}>Weet niet / anders</Radio>
                </Radio.Group>
            )}
        />
    )
}
