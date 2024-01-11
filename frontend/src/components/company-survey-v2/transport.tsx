import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {Cars} from './cars'
import {FormRow} from './generic/form-row'
import {NumberInput} from './generic/number-input'
import {NumberRow} from './generic/number-row'
import {ProjectName} from './project'
import {Trucks} from './trucks'
import {Vans} from './vans'

export const Transport = ({form, prefix, project}: {
    form: UseFormReturn,
    prefix: string,
    project: ProjectName
}) => {
    const {register, watch} = form

    const hasVehicles = watch(`${prefix}.hasVehicles`)

    return (
        <>
            <h2>6. Mobiliteit</h2>
            <FormRow
                label="Hebben jullie bedrijfsauto's, -busjes, of -vrachtwagens?"
                WrappedInput={BooleanInput}
                name={`${prefix}.hasVehicles`}
                form={form} />
            {hasVehicles && (
                <>
                    <Trucks form={form} prefix={`${prefix}.trucks`} project={project} />
                    <Vans form={form} prefix={`${prefix}.vans`} project={project} />
                    <Cars form={form} prefix={`${prefix}.cars`} project={project} />
                </>
            )}
            <FormRow
                label="Aantal personenauto's voor woon-werk verkeer per dag?"
                WrappedInput={NumberInput}
                name={`${prefix}.numDailyCarCommuters`}
                form={form} />
            <NumberRow
                label="Hoeveel laadpunten voor woon-werk verkeer hebben jullie?"
                name={`${prefix}.numCommuterChargePoints`}
                form={form} />
        </>
    )
}
