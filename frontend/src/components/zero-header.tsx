import React, {FunctionComponent, PropsWithChildren, useState} from "react"
import {Button} from "primereact/button";
import {Sidebar} from "primereact/sidebar";
import {css} from "@emotion/react";

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

export const ZeroHeader: FunctionComponent<PropsWithChildren & {
    setActiveComponent: (component: string) => void,
}> = ({setActiveComponent}) => {
    const [visible, setVisible] = useState(false);

    return (
        <div className="app-header">
            <div className="header" css={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '1em 1em',
                backgroundColor: '#f5f5f5',
                boxShadow: '1px solid #ddd'
            }}>
                <Button icon="pi pi-bars" onClick={() => setVisible(true)}/>
                <div>
                    <img
                        alt="Zenmo logo"
                        src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png"
                        style={{height: "1.5em", verticalAlign: "sub"}}/>
                    &nbsp;
                    <b>Zenmo Zero</b>
                </div>
            </div>
            <Sidebar visible={visible} position="left" onHide={() => setVisible(false)} css={sidebarStyle}>
                <Button label="Users" icon="pi pi-fw pi-user" onClick={() => {
                    setVisible(false);
                    setActiveComponent('Users')
                }} css={buttonStyle}/>
                <Button label="Projects" icon="pi pi-fw pi-briefcase" onClick={() => {
                    setVisible(false);
                    setActiveComponent('Projects')
                }} css={buttonStyle}/>
                <Button label="Surveys" icon="pi pi-fw pi-file" onClick={() => {
                    setVisible(false);
                    setActiveComponent('Surveys')
                }} css={buttonStyle}/>
                <Button label="About Us" icon="pi pi-fw pi-info-circle" onClick={() => {
                    setVisible(false);
                    setActiveComponent('About Us')
                }} css={buttonStyle}/>
            </Sidebar>
        </div>
    );
};