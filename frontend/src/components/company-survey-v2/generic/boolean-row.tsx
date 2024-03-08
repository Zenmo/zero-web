
import {FormRow} from "./form-row";
import {FunctionComponent} from "react";
import {NumberRowProps} from "./number-row";
import {BooleanInput} from "./boolean-input";

export const BooleanRow: FunctionComponent<NumberRowProps> = (props) => (
    <FormRow {...props} WrappedInput={BooleanInput} />
)
