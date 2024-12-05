
import React, {FunctionComponent, useState} from 'react';
import { Sidebar } from 'primereact/sidebar';
import { Button } from 'primereact/button';
import { css } from '@emotion/react';

import 'primereact/resources/themes/saga-blue/theme.css';  // Choose the theme you prefer
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import {Admin} from "../admin/admin";
import {Intro} from "./intro";

const sidebarStyle = css({
    width: '250px',
    backgroundColor: '#f5f5f5',
    borderRight: '1px solid #ddd',
});

const buttonStyle = css({
    display: 'block',
    marginBottom: '45px',
    width: '100%',
    textAlign: 'left',
    padding: '0.5em 1em',
    border: 'none',
    borderBottom: '1px solid #ddd',
    color: '#333',
    background: '#f5f5f5',
    transition: 'background-color 0.2s ease-in-out',
    fontWeight: 'normal',
    cursor: 'pointer',
    '&:hover': {
        backgroundColor: '#ebebeb',
        color: '#007ad9',
    },
});

export const Home: FunctionComponent = () => {

    const [visible, setVisible] = useState(true);
    const [activeComponent, setActiveComponent] = useState<string>('Dashboard');
    
    const renderContent = () => {
        switch (activeComponent) {
            case 'Users':
                return <div>Users Content</div>;
            case 'Projects':
                return <div>Projects Content</div>;
            case 'Surveys':
                return <div><Admin ></Admin></div>
            case 'About Us':
                return <div><Intro /></div>;
            default:
                return <div>Dashboard Content</div>;
        }
    };

    return (
        <div className="app-layout">
            <Sidebar visible={visible} position="left" onHide={() => setVisible(false)} css={sidebarStyle}>
                <Button label="Users" icon="pi pi-fw pi-user" onClick={() => {setVisible(false); setActiveComponent('Users')}} css={buttonStyle}/>
                <Button label="Projects" icon="pi pi-fw pi-briefcase" onClick={() => {setVisible(false); setActiveComponent('Projects')}} css={buttonStyle}/>
                <Button label="Surveys" icon="pi pi-fw pi-file" onClick={() => {setVisible(false); setActiveComponent('Surveys')}} css={buttonStyle}/>
                <Button label="About Us" icon="pi pi-fw pi-info-circle" onClick={() => {setVisible(false); setActiveComponent('About Us')}} css={buttonStyle}/>
            </Sidebar>
            <div className="main-content"  css={{ flexGrow: 1, padding: '1em' }}>
                <Button icon="pi pi-bars" onClick={() => setVisible(true)} />
                {renderContent()}
            </div>
        </div>
    );
};