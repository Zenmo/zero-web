import {MapContainer, Marker, Popup, TileLayer} from "react-leaflet";
import {LatLngTuple} from "leaflet";
import "leaflet/dist/leaflet.css";

const disruptor: LatLngTuple = [51.44971831403754, 5.4947035381928035]

export const MainMap = () => (
    <MapContainer center={disruptor} zoom={13} scrollWheelZoom={false} style={{width: "500px", height: "500px"}}>
        <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <Marker position={disruptor}>
            <Popup>
                A pretty CSS3 popup. <br/> Easily customizable.
            </Popup>
        </Marker>
    </MapContainer>
)
