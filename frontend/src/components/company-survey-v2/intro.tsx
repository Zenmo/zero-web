import {FunctionComponent} from 'react'

export const Intro: FunctionComponent = () => {
    return (
        <>
            <h1>Opgeven energieprofiel bedrijf</h1>
            <p>
                Dit formulier is bedoeld voor een bedrijf om zijn situatie te omschrijven,
                zodat dit verwerkt kan worden in de simulatie van een bedrijventerrein.
                Dit project heeft tot doel om het energiesysteem te verduurzamen en oplossingen te zoeken voor
                netcongestie.
                De opdrachtgever is XXXXXXX en de uitvoerder is ZEnMo Simulations. ZEnMo staat voor Zero emission Energy
                and Mobility.
            </p>
            <p>
                Het aantal vrije invulvelden is beperkt zodat de gegevens direct in de simulatie gebruikt kunnen worden.
            </p>
        </>
    )
}