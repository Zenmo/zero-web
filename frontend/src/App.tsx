import React, {FunctionComponent} from 'react'
import {ZeroHeader} from "./components/zero-header";
import {Outlet} from "react-router-dom";
import {Dashboard} from "./components/dashboard";

export const App: FunctionComponent = () => {
    return (
        <>
            <ZeroHeader />
            <Outlet />
        </>
    );
};
