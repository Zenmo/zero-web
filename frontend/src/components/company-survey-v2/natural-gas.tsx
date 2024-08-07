import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {NumberRow} from './generic/number-row'
import {Purpose, Upload} from './generic/upload'
import {ProjectName} from './project'
import {EanRow} from "./ean-row"

export const NaturalGas =  ({form, prefix, project}: { form: UseFormReturn , prefix: string, project: ProjectName }) => {
    const {watch, register} = form

    const hasConnection = watch(`${prefix}.hasConnection`)

    return (
        <>
            <h2>4. Aardgas</h2>
            <FormRow
                label="Heeft uw bedrijf op dit adres een aansluitcontract voor gas?"
                name={`${prefix}.hasConnection`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasConnection && (
                <>
                    <EanRow form={form} name={`${prefix}.ean`} />
                    <NumberRow
                        label="Wat is min of meer het jaarlijkse verbruik?"
                        name={`${prefix}.annualDelivery_m3`}
                        form={form}
                        suffix="m3" />
                    <NumberRow
                        label="Welk deel daarvan wordt naar uw inschatting gebruikt voor verwarming van ruimtes en water?"
                        name={`${prefix}.percentageUsedForHeating`}
                        form={form}
                        options={{min: 0, max: 100}}
                        suffix="%" />
                    <LabelRow label="Upload hier uw data van uw gasverbruik per uur indien u daarover beschikt (excel of .csv)">
                        <Upload
                            multiple={true}
                            setFormValue={files => form.setValue(`${prefix}.hourlyValuesFiles`, files)}
                            company={form.watch('companyName')}
                            project={project}
                            purpose={Purpose.NATURAL_GAS_VALUES} />
                    </LabelRow>
                </>
            )}
        </>
    )
}
