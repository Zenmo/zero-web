import 'leaflet/dist/leaflet.css'
import React, {createElement as h, FunctionComponent, useState} from 'react'
import './App.css'
import {AggregatedAreaData} from './components/aggregated-area-data'
import {AnyLogic} from './components/any-logic'
import {BuurtPicker} from './components/buurt-picker'
import {MainMap} from './components/main-map'
import {PandDataDisplay} from './components/pand-display'
import {useApp} from './services/appState'
import {assertDefined} from './services/util'
import {Buurt} from './services/wijkenbuurten/buurten'
import {ZeroLayout} from "./components/zero-layout"
import {Content} from "./components/Content";

export const Simulation: FunctionComponent<{}> = () => {
    const appHook = useApp()
    const {setGeometry, getPandData, bag2dPanden} = appHook

    const [currentPandId, setCurrentPandId] = useState('')

    const [buurt, setBuurt] = useState<Buurt | undefined>()

    return (
        <Content>
            <ZeroLayout subtitle="Simuleer je buurt">
                {/* Three-column layout*/}
                <div className={"d-flex flex-row gap-3"}>
                    <div className={"col-3 pt-4"}>
                        {h(AggregatedAreaData, {appHook: appHook})}
                        <BuurtPicker onSelectBuurt={buurt => {
                            setBuurt(buurt)
                            setGeometry(buurt.geometry)
                        }}/>
                    </div>
                    <div
                        className={"d-flex flex-grow-1 vh-100"}>
                        <MainMap bag2dPanden={bag2dPanden}
                                 setGeometry={setGeometry}
                                 setCurrentPandId={setCurrentPandId}
                                 buurt={buurt}/>
                        <AnyLogic appHook={appHook}/>
                    </div>
                    {currentPandId && getPandData(currentPandId) &&
                        <div className={"col-3"}>
                            <PandDataDisplay pandData={assertDefined(getPandData(currentPandId))}/>
                        </div>
                    }
                </div>
            </ZeroLayout>
        </Content>
    )
}

