import React from 'react'
import ReactDOM from 'react-dom/client'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import './index.css'
import {DE_WIEKEN, HESSENPOORT} from './components/company-survey-v2/project'
import {Survey} from './components/company-survey-v2/survey'
import {ThankYou} from './components/thank-you'
import reportWebVitals from './reportWebVitals'
import {ZeroHeader} from "./components/zero-header";
import App from "./App";

const router = createBrowserRouter([
    {
        path: "/",
        element: <ZeroHeader />,
    },
    {
        path: "/proof-of-concept",
        element: <App />,
    },
    {
        path: "/bedrijven-hessenpoort",
        element: <Survey project={HESSENPOORT} />,
    },
    {
        path: "/bedrijven-de-wieken",
        element: <Survey project={DE_WIEKEN} />,
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
