import React, {FunctionComponent} from 'react'
import {ZeroLayout} from "./components/zero-layout";
import {Outlet} from "react-router-dom";

export const App: FunctionComponent = () => {
    return (
        <>
            <ZeroLayout />
            <Outlet />
        </>
    );
};
