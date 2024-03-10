import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {TextAreaRow} from './generic/text-area-row'
import {MissingPvReason} from './missing-pv-reason'
import {PVOrientation} from './pv-orientation'

export const Supply = ({form, prefix, hasSupplyName}: {form: UseFormReturn, prefix: string, hasSupplyName: string}) => {
    const { register, watch } = form

    const pvInstalledKwp = watch(`${prefix}.pvInstalledKwp`)
    const pvPlanned = watch(`${prefix}.pvPlanned`)
    const hasSupply = watch(hasSupplyName)

    return (
        <>
            <h3>Elektriciteitsopwek op deze netaansluiting</h3>
            <FormRow
                label="Is er elektriciteitsopwek op deze netaansluiting?"
                name={hasSupplyName}
                form={form}
                WrappedInput={BooleanInput} />
            {hasSupply && (
                <>
                    <NumberRow
                        label="Hoe veel opgesteld vermogen zonnepanelen heeft uw bedrijf?"
                        name={`${prefix}.pvInstalledKwp`}
                        form={form}
                        suffix="kWp" />
                    {pvInstalledKwp > 0 && (
                        <>
                            <FormRow
                                label="Wat is de orientatie van dit opgesteld vermogen zonnepanelen?"
                                name={`${prefix}.pvOrientation`}
                                form={form}
                                WrappedInput={PVOrientation} />
                        </>
                    )}
                </>
            )}
            <FormRow
                label="Bent u van plan de komende 5 jaar zonnepanelen (bij) te plaatsen?"
                name={`${prefix}.pvPlanned`}
                form={form}
                WrappedInput={BooleanInput} />
            {pvPlanned && (
                <>
                    <NumberRow
                        label="Hoe veel vermogen zonnepanelen heeft u gepland?"
                        name={`${prefix}.pvPlannedKwp`}
                        form={form}
                        suffix="kWp" />
                    <NumberRow
                        label="Welk jaar verwacht u deze zonnepanelen te plaatsen?"
                        name={`${prefix}.pvPlannedYear`}
                        form={form}
                        options={{min: 2023, max: 2050}}/>
                    <FormRow
                        label="Wat is de orientatie van deze zonnepanelen?"
                        name={`${prefix}.pvPlannedOrientation`}
                        form={form}
                        WrappedInput={PVOrientation} />
                </>
            )}
            {hasSupply === false && pvPlanned === false && (
                <FormRow
                    label="Waarom niet?"
                    name={`${prefix}.missingPvReason`}
                    form={form}
                    WrappedInput={MissingPvReason} />
            )}
            {hasSupply && (
                <>
                    <NumberRow
                        label="Heeft u (kleine) windmolens?"
                        name={`${prefix}.windInstalledKw`}
                        form={form}
                        suffix="kW" />
                    <NumberRow
                        label="Bent u van plan windmolens te plaatsen?"
                        name={`${prefix}.windPlannedKw`}
                        form={form}
                        suffix="kW" />
                    <TextAreaRow
                        label="Heeft u andere vormen elektriciteitsproductie (bijv. dieselgenerator of waterstof)? Zo ja, kunt u kort beschrijven welke, hoeveel het ingezet wordt, en wat het vermogen is?"
                        form={form}
                        name={`${prefix}.otherSupply`} />
                </>
            )}
        </>
    )
}