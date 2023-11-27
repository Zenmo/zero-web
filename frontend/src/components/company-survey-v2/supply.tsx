import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {PVOrientation} from './pv-orientation'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Supply = ({form, prefix, hasSupply}: {form: UseFormReturn, prefix: string, hasSupply: boolean | undefined}) => {
    const { register, watch } = form

    const pvInstalledKwp = watch(`${prefix}.pvInstalledKwp`)
    const pvPlanned = watch(`${prefix}.pvPlanned`)

    return (
        <>
            <h3>Opwek</h3>
            {hasSupply && (
                <>
                    <LabelRow label="Hoe veel opgesteld vermogen zonnepanelen heb je?">
                        <NumberInput {...register(`${prefix}.pvInstalledKwp`)} /> kWp
                    </LabelRow>
                    {pvInstalledKwp > 0 && (
                        <>
                            <LabelRow label="Wat is de orientatie van dit opgesteld vermogen zonnepanelen?">
                                <PVOrientation form={form} name={`${prefix}.pvOrientation`} />
                            </LabelRow>
                            <LabelRow label="Hoe veel opgesteld vermogen zonnepanelen heb je?">
                                <NumberInput {...register(`${prefix}.pvPlanned`)} />
                            </LabelRow>
                        </>
                    )}
                </>
            )}
            <LabelRow label="Zijn jullie van plan de komende jaren zonnepanelen te plaatsen?">
                <BooleanInput form={form} name={`${prefix}.pvPlanned`} />
            </LabelRow>
            {pvPlanned && (
                <>
                    <LabelRow label="Hoe veel vermogen zonnepanelen heb je gepland?">
                        <NumberInput {...register(`${prefix}.pvPlannedKwp`)} /> kWp
                    </LabelRow>
                    <LabelRow label="Welk jaar verwacht je deze zonnepanelen te plaatsen?">
                        <NumberInput {...register(`${prefix}.pvPlannedYear`)} min={2023} max={2050} />
                    </LabelRow>
                    <LabelRow label="Wat is de orientatie van deze zonnepanelen?">
                        <PVOrientation form={form} name={`${prefix}.pvOrientation`} />
                    </LabelRow>
                </>
            )}
            {hasSupply && (
                <>
                    <LabelRow label="Heeft u (kleine) windmolens?">
                        <NumberInput {...register(`${prefix}.windInstalledKw`)} /> kW
                    </LabelRow>
                    <LabelRow label="Heeft u naast zon en wind nog andere methoden van elektriciteitsproductie? Zo ja, welke en hoeveel geïnstalleerd vermogen?">
                        <input type="text" {...register(`${prefix}.otherSupply`)} />
                    </LabelRow>
                </>
            )}
        </>
    )
}