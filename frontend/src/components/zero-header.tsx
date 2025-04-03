import React, {FunctionComponent, PropsWithChildren, useState} from "react"
import {Button} from "primereact/button";
import {Sidebar} from "primereact/sidebar";
import {css} from "@emotion/react";
import {To, useNavigate} from "react-router-dom";
import {useUser} from "../user/use-user";
import {redirectToLogin} from "../admin/use-users";

const sidebarStyle = css({
    width: '16rem',
    backgroundColor: '#f5f5f5',
    borderRight: '1px solid #ddd',
});

const buttonStyle = css({
    display: 'block',
    marginBottom: '3rem',
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

export const ZeroHeader: FunctionComponent<PropsWithChildren & {}> = () => {
    const {isLoading, isLoggedIn, username, isAdmin} = useUser()

    const [visible, setVisible] = useState(false);
    const navigate = useNavigate();

    const loadContent = (navidateTo: To) => {
        setVisible(false);
        navigate(navidateTo)
    }
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
                <div className={'d-flex flex-row gap-3 align-items-center'}>
                    <Button icon="pi pi-bars" onClick={() => setVisible(true)} className={'rounded rounded-3'}/>
                    <span className={' fs-2 fw-bold text-gray-800'}>
                    Zenmo Zero
                </span>
                </div>

                <div style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                }}>
                    {!isLoggedIn && (
                        <Button
                            label="Log In"
                            className="p-button-text"
                            onClick={redirectToLogin}
                            css={{marginLeft: "auto", fontSize: "0.9em", cursor: "pointer"}}
                        />
                    )}
                    <a href="https://zenmo.com">
                        <img
                            alt="Zenmo logo"
                            className={'logo-default h-40px'}
                            src="https://zenmo.com/wp-content/uploads/2018/12/zenmo-logo-website-grey.png"
                        />
                    </a>
                </div>
            </div>

            <Sidebar visible={visible} position="left" onHide={() => setVisible(false)} css={sidebarStyle}>
                <a onClick={() => loadContent(`/`)} css={buttonStyle}>
                    <i className="fas fa-house me-2"></i>
                    Home
                </a>
                <a onClick={() => loadContent('/surveys')} css={buttonStyle}>
                    <i className="fas fa-file-contract me-2"></i>
                    Surveys
                </a>
                <a onClick={() => loadContent('/projects')} css={buttonStyle}>
                    <i className="fas fa-business-time me-2"></i>
                    Projects
                </a>
                {isAdmin && (
                    <a onClick={() => loadContent('/users')} css={buttonStyle}>
                        <i className="fas fa-users me-2"></i>
                        Users
                    </a>
                )}
                <a onClick={() => loadContent('/simulation')} css={buttonStyle}>
                    <i className="fas fa-chart-line me-2"></i>
                    Simulation
                </a>
            </Sidebar>
        </div>
    );
};