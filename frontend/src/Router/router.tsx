import { createBrowserRouter } from "react-router-dom";

import {Users} from "../admin/users";
import {Projects} from "../admin/projects";
import {Surveys} from "../admin/surveys";

import {ThankYou} from '../components/thank-you'
import {LoginWidget} from "../user/login";
import {BedrijvenFormV1} from "../components/bedrijven-form-v1";
import Simulation from "../Simulation";

import {Survey} from '../components/company-survey-v2/survey'
import {Intro} from "../components/intro";
import {SurveyById, SurveyByIdRouteData} from "../components/company-survey-v2/survey-by-id";
import {ExcelImport} from "../excel-import/excel-import"
import {NewSurveyByProjectName} from "../components/company-survey-v2/new-survey-by-project-name"
import {DE_WIEKEN, HESSENPOORT} from '../components/company-survey-v2/project'
import {Dashboard} from "../components/dashboard";
import {App} from "../App";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            {path: "", element: <Dashboard />},
            {path: "/users", element: <Users />},
            {path: "/projects", element: <Projects />},
            {path: "/surveys", element: <Surveys />},
            {path: "/simulation", element: <Simulation />},
            {path: "/intro", element: <Intro />},
        ],
    },
    {
        path: "/bedrijven-v1",
        element: <BedrijvenFormV1 />
    },
    {
        path: "/new-survey/:projectName",
        element: <NewSurveyByProjectName />,
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
        path: "/bedrijven-uitvraag/:surveyId",
        element: <SurveyById />,
        loader: ({params: {surveyId}, request}): SurveyByIdRouteData => {
            if (!surveyId) {
                throw new Error("Survey ID is required")
            }
            const url = new URL(request.url);
            const deeplink = url.searchParams.get("deeplink");
            const secret = url.searchParams.get("secret");

            return {
                surveyId,
                deeplink,
                secret,
            }
        }
    },
    {
        path: "/bedankt",
        element: <ThankYou />,
    },
    {
        path: "/admin/import-excel",
        element: <ExcelImport />
    },

    {
        path: "/login",
        element: <LoginWidget />
    }
]);

