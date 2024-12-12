import React, {FunctionComponent} from 'react'
import {Outlet} from "react-router-dom";

export const App: FunctionComponent = () => {
    return (
        <>
            <Outlet/>
        </>
    );
};