import {FunctionComponent} from 'react'
import {Project} from './project'

export const Intro: FunctionComponent<{project: Project}> = ({project}) => {
    return (
        <>
            <h1>Data formulier energie en mobiliteit {project.name}</h1>
            <p>
                Met dit formulier halen we de energie- en mobiliteitsdynamieken op van {project.name}, zowel in de
                huidige als in toekomstige situatie. Samen met gegevens van de netbeheerder worden deze dynamieken
                nagespeeld in de digital twin. Het doel is om daarmee gezamelijk te verkennen welke congestie-
                problemen zich voordoen, welke oplossingen er mogelijk zijn en welke uitbreidingsplannen de komende
                jaren wel of niet doorgezet kunnen worden.
            </p>
            <p>
                Alle ingevulde gegevens worden vertrouwlijk behandeld en blijven eigendom van de bedrijven zelf. 
                Parkmanagement {project.name} beheert de gegevens tijdens het project en Zenmo Simulations laadt ze
                in de digtial twin.
            </p>
            <p>
                Het invullen zal circa 5 tot 15 minuten duren. Echter, het is mogelijk dat het één en ander 
                opgezocht moet worden. Bijvoorbeeld het gecontracteerd (teruglever)vermogen, inloggegevens van jullie
                meetbedrijf, of het aantal vrachtwagens. Ook bij de paar bedrijven met meerdere netaansluitingen zal 
                het wat meer tijd kosten. 
            </p>
            <p>
                Als dit formulier niet geschikt is om jullie situatie te beschrijven, of als jullie noemenswaardig 
                interessante energie dynamieken hebben, dan kunnen jullie dat ons via de mail ter attentie brengen
                via <a href={`mailto:${project.email}`}>{project.email}</a>. We kunnen jullie situatie dan realistischer meenemen
                in de digital twin.
            </p>
        </>
    )
}