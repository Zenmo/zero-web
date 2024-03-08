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
            {watch(`${prefix}.hasOtherVehicles`) > 0 && (
                <LabelRow label="In hoeverre zijn deze andere voertuigen elektrisch?">
                    <div style={{display: 'flex'}}>
                        <Controller
                            control={form.control}
                            name={`${prefix}.electricRatio`}
                            render={({field: {onChange, value}}) => (
                                <input
                                    type="range"
                                    min="0"
                                    max="1"
                                    step="0.01"
                                    value={value ?? 0}
                                    onChange={e => onChange(e.target.value)}/>
                            )}
                        />
                        <span>&nbsp;&nbsp;{Math.round(watch(`${prefix}.electricRatio`, 0) * 100)} %</span>
                    </div>
                </LabelRow>
            )}
        </>
    )
}