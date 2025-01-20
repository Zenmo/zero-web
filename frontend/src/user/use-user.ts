import {useOnce} from "../hooks/use-once";
import {useState, useEffect} from "react";
import {User, userFromJson } from "zero-zummon"

type UseUserReturn = {
    isLoading: boolean,
    isLoggedIn?: boolean,
    username?: string,
    isAdmin?: boolean,
}

export const useUser = (): UseUserReturn => {
    const [userId, setUserId] = useState<null>()

    const [state, setState] = useState<UseUserReturn>({
        isLoading: true,
        isLoggedIn: undefined,
        username: undefined,
        isAdmin: false,
    })

    useOnce(async () => {
        try {
            const response = await fetch(import.meta.env.VITE_ZTOR_URL + "/user-info", {
                credentials: "include",
            })

            if (response.status === 401) {
                redirectToLogin()
                return
            }
            if (!response.ok) {
                throw new Error(`Failed to get user: ${response.statusText}`)
            }

            if (response.ok) {
                const userInfo = await response.json();
                setUserId(userInfo.decodedAccessToken.sub);

                setState((prevState) => ({
                    ...prevState,
                    isLoading: false,
                    isLoggedIn: true,
                    username: userInfo.decodedAccessToken.preferred_username,
                    isAdmin: userInfo.isAdmin
                }));
            }
        } catch (e) {
            console.error(e)
        } finally {
            setState(prevState => ({
                ...prevState,
                isLoading: false,
            }))
        }
    })

    return state
}

export const redirectToLogin = () => {
    window.location.href = import.meta.env.VITE_ZTOR_URL + '/login?redirectUrl=' + encodeURIComponent(window.location.href)
}