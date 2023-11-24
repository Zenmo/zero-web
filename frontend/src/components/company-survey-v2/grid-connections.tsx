import {useEffect} from 'react'
import {useFieldArray, UseFormReturn} from 'react-hook-form'
import {GridConnection} from './grid-connection'

export const GridConnections = ({form}: { form: UseFormReturn }) => {
    const {register, control} = form
    const name = "gridConnections"

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray({
        control,
        name,
    })

    return (
        <>
            <h2>Netaansluiting</h2>
            <button>Extra netaansluiting</button>
            {fields.map((item, index) => {
                return (
                    <GridConnection key={index} form={form} prefix={`${name}.${index}.`} />
                )
            })}
        </>
    )
}
