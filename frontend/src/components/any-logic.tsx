
import {useRef, useState} from "react";
import {startSimulation} from "../services/any-logic/any-logic-client";
import {AppState} from "../services/appState";
import {appStateToScenarioInput} from "../services/any-logic/scenario-input";

export const AnyLogic = ({appState}: {appState: AppState}) => {

    const divId = 'any-logic'

    const [visible, setVisible] = useState(false)

    const onStartSimulation = async () => {
        setVisible(true)
        await startSimulation(divId, appState)
    }

    const onViewInput = () => {
        const input = JSON.stringify(appStateToScenarioInput(appState))
        const newWindow = window.open("data:application/json," + encodeURIComponent(input), "_blank");
        if (newWindow === null) {
            throw new Error("Can't open new window")
        }
        newWindow.focus();
    }

    const ref = useRef(null)

    return (
        <>
            <div>
                <button onClick={onStartSimulation}>Start simulatie</button>
                <button onClick={onViewInput}>Bekijk simulatie input</button>
            </div>
            <div id={divId} ref={ref} style={{flexGrow: 1, display: visible ? 'block': 'none'}}>
                <p>Bezig met laden...</p>
            </div>
        </>
    )
}