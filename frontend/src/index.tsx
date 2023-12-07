import React from 'react'
import ReactDOM from 'react-dom/client'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import App from './App'
import './index.css'
import {BedrijvenFormV1} from './components/bedrijven-form-v1'
import {Survey} from './components/company-survey-v2/survey'
import {ThankYou} from './components/thank-you'
import reportWebVitals from './reportWebVitals'

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
    },
    {
        path: "/bedrijven-drechtsteden",
        element: <BedrijvenFormV1 />,
    },
    {
        path: "/bedrijven-hessenpoort",
        element: <Survey project={{name: 'Hessenpoort', email: 'info@ondernemersvereniginghessenpoort.nl'}} />,
    },
    {
        path: "/bedrijven-de-wieken",
        element: <Survey project={{name: 'De Wieken', email: 'info@zenmo.com'}} />,
    },
    {
        path: "/bedankt",
        element: <ThankYou />,
    }
]);

const root = ReactDOM.createRoot(
    document.getElementsByTagName('body')[0],
)
root.render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>,
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals()
