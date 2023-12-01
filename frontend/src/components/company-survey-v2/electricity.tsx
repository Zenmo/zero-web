import {Radio} from 'antd'
import {useState} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {ConsumptionProfileSelect} from './consumption-profile-select'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {KleinverbruikCapacityInput} from './kleinverbruik-capacity-input'
import {LabelRow} from './generic/label-row'
import {ConsumptionSpec, ElectricityConsumptionRadios} from "./electricity-consumption-radios";

enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const Electricity = ({form, prefix, hasSupplyName}: {
    form: UseFormReturn,
    prefix: string,
    hasSupplyName: string
}) => {
    const {register} = form

    const hasSupply = form.watch(hasSupplyName)

    const [consumptionSpec, setConsumptionSpec] = useState<ConsumptionSpec|null|undefined>()
    const [connectionType, setConnectionType] = useState<ConnectionType>()

    return (
        <>
            {/*<FormRow label="EAN" name={`${prefix}.ean`} form={form} />*/}
            <LabelRow label="Type aansluiting">
                <Radio.Group onChange={e => setConnectionType(e.target.value)} value={connectionType}>
                    <Radio value={ConnectionType.GROOTVERBRUIK} css={{display: 'block'}}>Grootverbruik</Radio>
                    <Radio value={ConnectionType.KLEINVERBRUIK} css={{display: 'block'}}>Kleinverbruik</Radio>
                </Radio.Group>
            </LabelRow>
            <LabelRow label="Hoe wilt u het elektriciteitsprofiel van deze netaansluiting doorgeven?">
                <ElectricityConsumptionRadios onChange={setConsumptionSpec} consumptionSpec={consumptionSpec} />
            </LabelRow>
            <FormRow
                label="Is er ook elektriciteitsopwek op deze netaansluiting?"
                name={hasSupplyName}
                form={form}
                WrappedInput={BooleanInput} />
            {consumptionSpec === ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES && (
                <LabelRow label="Kwartierwaarden">
                    <input type="file" {...register(`${prefix}.quarterHourlyValuesFiles`)} />
                </LabelRow>
            )}
            {consumptionSpec === ConsumptionSpec.SPECTRAL_AUTHORIZATION && (
                <p>Spectral machtiging TODO</p>
            )}
            {/*{consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (*/}
            {/*    <>*/}
            {/*        <NumberRow*/}
            {/*            label="Jaarverbruik"*/}
            {/*            name={`${prefix}.annualElectricityDemandKwh`}*/}
            {/*            form={form}*/}
            {/*            suffix="kWh" />*/}
            {/*        {hasSupply && (*/}
            {/*            <NumberRow*/}
            {/*                label="Jaaropwek"*/}
            {/*                name={`${prefix}.annualElectricityProductionKwh`}*/}
            {/*                form={form}*/}
            {/*                suffix="kWh" />*/}
            {/*        )}*/}
            {/*    </>*/}
            {/*)}*/}
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
                        label="Wat is uw gecontracteerde vermogen voor elektriciteitsafname?"
                        name={`${prefix}.grootverbruik.contractedConnectionDemandCapacityKw`}
                        form={form}
                        suffix="kW" />
                    {hasSupply && (
                        <NumberRow
                            label="Wat is uw gecontracteerde vermogen voor elektriciteitsteruglevering?"
                            name={`${prefix}.grootverbruik.contractedConnectionSupplyCapacityKw`}
                            form={form}
                            suffix="kW" />
                    )}
                </>
            )}
        </>
    )
}
