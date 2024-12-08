
import React, {FunctionComponent, useState} from 'react';

import 'primereact/resources/themes/saga-blue/theme.css';  // Choose the theme you prefer
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import {ZeroHeader} from "./zero-header";
import {Surveys} from "../admin/surveys";
import {Users} from "../admin/users";
import {Projects} from "../admin/projects";
import {Intro} from "./intro";
import {Dashboard} from "./dashboard";

export const Home: FunctionComponent = () => {
    const [activeComponent, setActiveComponent] = useState<string>('Dashboard');

    const renderContent = () => {
        switch (activeComponent) {
            case 'Users':
                return <div><Users /></div>;
            case 'Projects':
                return <div><Projects /></div>;
            case 'Surveys':
                return <div><Surveys /></div>
            case 'About Us':
                return <div><Intro /></div>;
            default:
                return <div><Dashboard /></div>;
        }
    };

    return (
        <div className="app-layout">
            <ZeroHeader setActiveComponent={setActiveComponent}/>
            <div className="main-content" css={{flexGrow: 1, padding: '1em'}}>
                {renderContent()}
            </div>
        </div>
    );
};