import {Radio} from 'antd'
import {Controller, UseFormReturn} from 'react-hook-form'

enum ConnectionCapacity {
    // The total number of kleinverbruik postal code regions is 359.299.
    // This statistic includes residential connections.
    _1x40A = "1x40A", // majority of connections in 6.491 postalcodes
    _1x50A = "1x50A", // majority of connections in 2.226 postalcodes
    _3x25A = "3x25A", // majority of connections in 125.261 postalcodes
    _3x35A = "3x35A", // majority of connections in 1.049 postalcodes
    _3x50A = "3x50A", // majority of connections in 180 postalcodes
    _3x63A = "3x63A", // majority of connections in 261 postalcodes
    _3x80A = "3x80A", // majority of connections in 613 postalcodes
}

export const KleinverbruikCapacityInput = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    {Object.values(ConnectionCapacity).map(capacity => (
                        <Radio key={capacity} value={capacity} css={{display: 'block'}}>{capacity}</Radio>
                    ))}
                </Radio.Group>
            )}
        />
    )
}