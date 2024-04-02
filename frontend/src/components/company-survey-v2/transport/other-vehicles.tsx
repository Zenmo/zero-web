import {Controller, UseFormReturn} from "react-hook-form";
import {BooleanRow} from "../generic/boolean-row";
import {LabelRow} from "../generic/label-row";
import {FunctionComponent} from "react";

export const OtherVehicles: FunctionComponent<{ form: UseFormReturn, prefix: string}> = ({form, prefix}: { form: UseFormReturn, prefix: string}) => {
    const {register, watch} = form

    return (
        <>
            <h3>Andere voertuigen</h3>
            <BooleanRow
                label="Hebben jullie nog andere voertuigen zoals heftrucks of shovels?"
                name={`${prefix}.hasOtherVehicles`}
                form={form} />
            {watch(`${prefix}.hasOtherVehicles`) && (
                <span>(placeholder)</span>
            )}
        </>
    )
}