import {css} from '@emotion/react'
import {FunctionComponent} from 'react'
import {useForm, UseFormReturn} from 'react-hook-form'
import {GridConnections} from './grid-connections'
import {Intro} from './intro'
import {LabelRow} from './label-row'
import {Transport} from './transport'

export const Survey: FunctionComponent = () => {
    // @ts-ignore
    const form: UseFormReturn = useForm({
        defaultValues: {
            gridConnections: [{}],
        }
    })
    const { register, handleSubmit, formState: { errors } } = form
    const onSubmit = (data: any) => console.log(data)

    return (
        <div css={{
            width: '100%',
            minHeight: '100vh',
            backgroundColor: 'grey',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'flex-start',
        }}>
            <form onSubmit={handleSubmit(onSubmit)} css={{
                maxWidth: '40rem',
                backgroundColor: 'white',
                padding: '2rem',
                marginTop: '2rem',
            }}>
                <Intro />

                <LabelRow label="Naam bedrijf">
                    <input type="text" {...register("companyName", {required: true})} />
                </LabelRow>
                <LabelRow label="Naam contactpersoon">
                    <input type="text" {...register("personName", {required: true})} />
                </LabelRow>
                <LabelRow label="E-mailadres">
                    <input type="email" {...register("email")} />
                </LabelRow>
                <Transport form={form} />
                <GridConnections form={form} />
            </form>
        </div>
    )
}

