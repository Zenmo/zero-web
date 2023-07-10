import React, {useEffect} from 'react';
import './App.css';
import {MainMap} from "./MainMap";
import L from 'leaflet'
import "leaflet/dist/leaflet.css";

const h = React.createElement
let map: L.Map

function App() {
    return (
        <div className="App">
            <h1>Zenmo Zero</h1>
            <div style={{
                display: "flex",
                justifyContent: "center"}}
            >
                <MainMap />
            </div>
        </div>
    );
}

export default App;
