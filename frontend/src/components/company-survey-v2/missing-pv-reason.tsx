import {Radio} from 'antd'
import {Controller, UseFormReturn} from 'react-hook-form'

enum MissingPvReasonOptions {
    NO_SUITABLE_ROOF = "NO_SUITABLE_ROOF",
    NO_BACKFEED_CAPACITY = "NO_BACKFEED_CAPACITY",
    NOT_INTERESTED = "NOT_INTERESTED",
    OTHER = "OTHER",
}

export const MissingPvReason = ({form, name}: { form: UseFormReturn, name: string }) => {
    return (
        <Controller
            control={form.control}
            name={name}
            render={({field: {onChange, value, ref}}) => (
                <Radio.Group onChange={e => onChange(e.target.value)} value={value}>
                    <Radio value={MissingPvReasonOptions.NO_SUITABLE_ROOF} css={{display: 'block'}}>Geen geschikt dak</Radio>
                    <Radio value={MissingPvReasonOptions.NO_BACKFEED_CAPACITY} css={{display: 'block'}}>Geen teruglevercapaciteit beschikbaar</Radio>
                    <Radio value={MissingPvReasonOptions.NOT_INTERESTED} css={{display: 'block'}}>Geen interesse</Radio>
                    <Radio value={MissingPvReasonOptions.OTHER} css={{display: 'block'}}>Anders</Radio>
                </Radio.Group>
            )}
        />
    )
}
