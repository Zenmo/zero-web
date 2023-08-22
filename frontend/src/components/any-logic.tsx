import {useRef, useState} from 'react'
import {startSimulation} from '../services/any-logic/any-logic-client'
import {appStateToScenarioInput} from '../services/any-logic/scenario-input'
import {AppHook} from '../services/appState'

export const AnyLogic = ({appHook}: { appHook: AppHook }) => {

    const divId = 'any-logic'

    const [visible, setVisible] = useState(false)

    const onStartSimulation = async () => {
        setVisible(true)
        await startSimulation(divId, appHook)
    }

    const onViewInput = () => {
        const input = JSON.stringify(appStateToScenarioInput(appHook))
        const newWindow = window.open('data:application/json,' + encodeURIComponent(input), '_blank')
        if (newWindow === null) {
            throw new Error('Can\'t open new window')
        }
        newWindow.focus()
    }

    const ref = useRef(null)

    return (
        <>
            <div>
                <button onClick={onStartSimulation}>Start simulatie</button>
                <button onClick={onViewInput}>Bekijk simulatie input</button>
            </div>
            <div id={divId} ref={ref}
                 style={{flexGrow: 1, display: visible ? 'block' : 'none'}}>
                <p>Bezig met laden...</p>
            </div>
        </>
    )
}