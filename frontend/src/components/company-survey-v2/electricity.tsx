import {Radio} from 'antd'
import {useState} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {NumberRow} from './generic/number-row'
import {KleinverbruikCapacityInput} from './kleinverbruik-capacity-input'

enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const Electricity = ({form, prefix}: {
    form: UseFormReturn,
    prefix: string,
}) => {
    const hasConnection = form.watch(`${prefix}.hasConnection`)

    const [connectionType, setConnectionType] = useState<ConnectionType>()

    return (
        <>
            <h2>2. Elektriciteit</h2>
            <FormRow
                label={
                    <>
                        <div>Heeft uw bedrijf op dit adres een aansluitcontract voor elektriciteit met een netbeheerder?</div>
                    </>
                }
                name={`${prefix}.hasConnection`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasConnection &&
                <>
                    {/*<FormRow label="EAN" name={`${prefix}.ean`} form={form} />*/}
                    <LabelRow label="Heeft u een groot- of kleinverbruik aansluiting?">
                        <Radio.Group onChange={e => setConnectionType(e.target.value)} value={connectionType}>
                            <Radio value={ConnectionType.GROOTVERBRUIK} css={{display: 'block'}}>Grootverbruik</Radio>
                            <Radio value={ConnectionType.KLEINVERBRUIK} css={{display: 'block'}}>Kleinverbruik</Radio>
                        </Radio.Group>
                    </LabelRow>
                    {connectionType === ConnectionType.KLEINVERBRUIK && (
                        <>
                            <FormRow
                                label="Aansluitwaarde"
                                name={`${prefix}.kleinverbruik.connectionCapacity`}
                                WrappedInput={KleinverbruikCapacityInput}
                                form={form} />
                            {/*{consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (*/}
                            {/*    <FormRow*/}
                            {/*        label="Profiel verbruik"*/}
                            {/*        name={`${prefix}.kleinverbruik.consumptionProfile`}*/}
                            {/*        WrappedInput={ConsumptionProfileSelect}*/}
                            {/*        form={form} />*/}
                            {/*)}*/}
                        </>
                    )}
                    {connectionType === ConnectionType.GROOTVERBRUIK && (
                        <>
                            <NumberRow
                                label="Wat is uw gecontracteerd vermogen voor elektriciteitsafname?"
                                name={`${prefix}.grootverbruik.contractedConnectionDemandCapacityKw`}
                                form={form}
                                suffix="kW" />
                            <NumberRow
                                label="Wat is uw gecontracteerd vermogen voor elektriciteitsteruglevering?"
                                name={`${prefix}.grootverbruik.contractedConnectionSupplyCapacityKw`}
                                form={form}
                                suffix="kW" />
                            <NumberRow
                                label="Wat is de fysieke capaciteit van de netaansluiting?"
                                name={`${prefix}.grootverbruik.physicalCapacityKw`}
                                form={form}
                                suffix="kW" />
                        </>
                    )}
                </>
            }
        </>
    )
}
