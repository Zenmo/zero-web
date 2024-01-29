import {FunctionComponent} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {FormRow} from '../generic/form-row'
import {NumberInput} from '../generic/number-input'
import {NumberRow} from '../generic/number-row'

export const CommutersVisitors: FunctionComponent<{ form: UseFormReturn, prefix: string }> = ({form, prefix}) => (
    <>
        <h3>woon-werk verkeer en bezoekers</h3>
        <FormRow
            label="Aantal auto’s/busjes voor woon-werkverkeer per dag?"
            WrappedInput={NumberInput}
            name={`${prefix}.numDailyCarAndVanCommuters`}
            form={form}/>
        <NumberRow
            label="Aantal personenauto’s van bezoekers per dag?"
            name={`${prefix}.numDailyCarVisitors`}
            form={form}/>
        <NumberRow
            label="Hoeveel laadpunten zijn er specifiek voor woon-werkverkeer en bezoekers?"
            name={`${prefix}.numCommuterAndVisitorChargePoints`}
            form={form}/>
    </>
)