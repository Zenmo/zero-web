import {UseFormReturn} from "react-hook-form";
import {BooleanRow} from "../generic/boolean-row";
import {FunctionComponent} from "react";
import {TextAreaRow} from "../generic/text-area-row";

export const OtherVehicles: FunctionComponent<{ form: UseFormReturn, prefix: string}> = ({form, prefix}: { form: UseFormReturn, prefix: string}) => {
    const { watch} = form

    return (
        <>
            <h3>Andere voertuigen</h3>
            <BooleanRow
                label="Heeft u nog andere voertuigen zoals heftrucks of shovels?"
                name={`${prefix}.hasOtherVehicles`}
                form={form} />
            {watch(`${prefix}.hasOtherVehicles`) && (
                <TextAreaRow
                    label="Kunt u kort toelichten wat voor voertuigen dat zijn, hoe ze ongeveer ingezet worden, hoeveel, en de elektrificatie(plannen)?"
                    form={form}
                    name={`${prefix}.description`} />
            )}
        </>
    )
}