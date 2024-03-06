import {FunctionComponent} from "react";
import {UseFormReturn} from "react-hook-form";
import {BooleanInput} from "./generic/boolean-input";
import {FormRow} from "./generic/form-row";
import {NumberRow} from "./generic/number-row";
import {TextAreaRow} from "./generic/text-area-row";

export const GridExpansion: FunctionComponent<{
    form: UseFormReturn,
    prefix: string,
}> = ({form, prefix}) => (
    <>
        <h3>Uitbreiding</h3>
        <FormRow
            label="Heeft u een aanvraag uit staan bij de netbeheerder voor het uitbreiden van de aansluiting?"
            name={`${prefix}.hasRequestAtGridOperator`}
            form={form}
            WrappedInput={BooleanInput} />
        {form.watch(`${prefix}.hasRequestAtGridOperator`) && (
            <>
                <NumberRow
                    label="Met hoeveel kW of kVA?"
                    name={`${prefix}.requestedKW`}
                    form={form} />
                <TextAreaRow
                    label="Waarvoor is de uitbreiding?"
                    name={`${prefix}.reason`}
                    form={form} />
            </>
        )}
    </>
)