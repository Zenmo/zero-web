import {css} from '@emotion/react'
import {FunctionComponent} from 'react'
import {useForm, UseFormReturn} from 'react-hook-form'
import {FormRow} from './form-row'
import {GridConnections} from './grid-connections'
import {Intro} from './intro'
import {LabelRow} from './label-row'
import {Transport} from './transport'

export const Survey: FunctionComponent = () => {
    // @ts-ignore
    const form: UseFormReturn = useForm({
        shouldUseNativeValidation: true,
        defaultValues: {
            gridConnections: [{}],
        }
    })
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = form

    console.log('errors', errors)
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
                '& input:invalid': {
                    backgroundColor: '#fcc',
                    borderColor: 'red',
                },
            }}>
                <Intro />
                <FormRow label="Naam bedrijf" InputComponent="input" name="companyName" form={form} options={{required: true}} />

                <LabelRow label="Naam contactpersoon">
                    <input type="text" {...register("personName", {required: true})} />
                </LabelRow>
                <LabelRow label="E-mailadres">
                    <input type="email" {...register("email")} />
                </LabelRow>
                <Transport form={form} />
                <GridConnections form={form} />
                <button type="submit">Verstuur</button>
            </form>
        </div>
    )
}

