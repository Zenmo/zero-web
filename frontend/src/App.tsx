import React, {useEffect} from 'react';
import './App.css';
import {MainMap} from "./MainMap";
import L from 'leaflet'
import "leaflet/dist/leaflet.css";

const h = React.createElement
let map: L.Map

function App() {
    // useEffect(() => {
    //     console.log('effect')
    //     map = L.map('mymap', {
    //         center: [51.505, -0.09],
    //         zoom: 13
    //     });
    //     L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    //         maxZoom: 19,
    //         attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    //     }).addTo(map);
    //     return () => {
    //         map.remove()
    //     }
    // }, [])

    return (
        <div className="App">
            <h1>Zenmo 0.0</h1>
            <MainMap/>
        </div>
    );
}

export default App;
