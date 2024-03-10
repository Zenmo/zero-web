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
            label="Heeft u een aanvraag uitstaan bij de netbeheerder voor het uitbreiden van de aansluiting?"
            name={`${prefix}.hasRequestAtGridOperator`}
            form={form}
            WrappedInput={BooleanInput} />
        {form.watch(`${prefix}.hasRequestAtGridOperator`) && (
            <>
                <NumberRow
                    label="Hoeveel extra kW (ofwel kVA) heeft u aangevraagd?"
                    name={`${prefix}.requestedKW`}
                    form={form} />
                <TextAreaRow
                    label="Wat is de reden van de netuitbreiding?"
                    name={`${prefix}.reason`}
                    form={form} />
            </>
        )}
    </>
)