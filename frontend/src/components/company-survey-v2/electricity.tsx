import {Radio} from 'antd'
import {useState} from 'react'
import {Controller, UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {ConsumptionProfileSelect} from './consumption-profile-select'
// import {ConsumptionProfile} from './consumption-profile'
import {KleinverbruikCapacityInput} from './kleinverbruik-capacity-input'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'
import {Supply} from './supply'

enum ConsumptionSpec {
    AUTHORIZATION = "AUTHORIZATION",
    UPLOAD_QUARTER_HOURLY_VALUES = "UPLOAD_QUARTER_HOURLY_VALUES",
    ANNUAL_VALUES = "ANNUAL_VALUES",
}

enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const Electricity = ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    const {register} = form

    const [consumptionSpec, setConsumptionSpec] = useState<ConsumptionSpec>()
    const [connectionType, setConnectionType] = useState<ConnectionType>()

    const hasSupplyName = `${prefix}.supply.hasSupply`
    const hasSupply = form.watch(hasSupplyName)

    return (
        <>
            <LabelRow label="EAN">
                <input type="text" {...register(`${prefix}.ean`, {required: false})} />
            </LabelRow>
            <LabelRow label="Type aansluiting">
                <Radio.Group onChange={e => setConnectionType(e.target.value)} value={connectionType}>
                    <Radio value={ConnectionType.GROOTVERBRUIK} css={{display: 'block'}}>Grootverbruik</Radio>
                    <Radio value={ConnectionType.KLEINVERBRUIK} css={{display: 'block'}}>Kleinverbruik</Radio>
                </Radio.Group>
            </LabelRow>
            <LabelRow label="Hoe wilt u het verbruik opgeven?">
                <Radio.Group onChange={e => setConsumptionSpec(e.target.value)} value={consumptionSpec}>
                    <Radio value={ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES} css={{display: 'block'}}>Kwartierwaarden uploaden (voorkeur)</Radio>
                    <Radio value={ConsumptionSpec.AUTHORIZATION} css={{display: 'block'}}>Machting geven</Radio>
                    <Radio value={ConsumptionSpec.ANNUAL_VALUES} css={{display: 'block'}}>Jaarverbruik invullen</Radio>
                </Radio.Group>
            </LabelRow>
            <LabelRow label="Heeft u energieopwek (bv. zon-op-dak)?">
                <BooleanInput form={form} name={hasSupplyName} />
            </LabelRow>
            {consumptionSpec === ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES && (
                <LabelRow label="Kwartierwaarden">
                    <input type="file" {...register(`${prefix}.houseNumber`)} />
                </LabelRow>
            )}
            {consumptionSpec === ConsumptionSpec.AUTHORIZATION && (
                <p>Machtiging TODO</p>
            )}
            {consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (
                <>
                    <LabelRow label="Jaarverbruik">
                        <NumberInput {...register(`${prefix}.annualElectricityDemandKwh`, {required: false})} /> kWh
                    </LabelRow>
                    {hasSupply && (
                        <LabelRow label="Jaaropwek">
                            <NumberInput {...register(`${prefix}.annualElectricityProductionKwh`)} /> kWh
                        </LabelRow>
                    )}
                </>
            )}
            {connectionType === ConnectionType.KLEINVERBRUIK && (
                <>
                    <LabelRow label="Aansluitwaarde">
                        <KleinverbruikCapacityInput form={form} name={`${prefix}.kleinverbruik.connectionCapacity`} />
                    </LabelRow>
                    {consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (
                        <LabelRow label="Profiel verbruik">
                            <ConsumptionProfileSelect form={form} name={`${prefix}.kleinverbruik.consumptionProfile`} />
                        </LabelRow>
                    )}
                </>
            )}
            {connectionType === ConnectionType.GROOTVERBRUIK && (
                <>
                    <LabelRow label="Wat is uw gecontracteerde vermogen voor elektriciteitsafname?">
                        <NumberInput {...register(`${prefix}.grootverbruik.contractedConnectionDemandCapacityKw`)} /> kWh
                    </LabelRow>
                    {hasSupply && (
                        <LabelRow label="Wat is uw gecontracteerde vermogen voor elektriciteitsteruglevering?">
                            <NumberInput {...register(`${prefix}.grootverbruik.contractedConnectionSupplyCapacityKw`)} /> kWh
                        </LabelRow>
                    )}
                </>
            )}
            <Supply form={form} prefix={`${prefix}.supply.`} hasSupply={hasSupply} />
        </>
    )
}
