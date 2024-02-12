import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from '../generic/boolean-input'
import {FormRow} from '../generic/form-row'
import {ProjectName} from '../project'
import {Cars} from './cars'
import {CommutersVisitors} from './commuters-visitors'
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
                form={form}/>
            <CommutersVisitors  form={form} prefix={prefix} />
            {hasVehicles && (
                <>
                    <Trucks form={form} prefix={`${prefix}.trucks`} project={project}/>
                    <Vans form={form} prefix={`${prefix}.vans`} project={project}/>
                    <Cars form={form} prefix={`${prefix}.cars`} project={project}/>
                </>
            )}
        </>
    )
}
