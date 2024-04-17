import {FunctionComponent} from "react";

export const Login: FunctionComponent = () => {
    return (
        <a href={process.env.ZTOR_URL + "/login?redirectUrl=" + encodeURIComponent(location.href)}>Login</a>
    )
}
