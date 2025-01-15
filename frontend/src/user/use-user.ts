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

    useEffect(() => {
        if (userId) {
            const fetchUser = async () => {
                try {
                    const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/users/${userId}`, {
                        credentials: 'include',
                    })
                    if (response.ok) {
                        const userData = await response.json();
                        setState((prevState) => ({
                            ...prevState,
                            isLoading: false,
                            isLoggedIn: true,
                            isAdmin: userData.isAdmin || false,
                        }));
                    } else {
                        alert(`Error fetching user: ${response.statusText}`);
                    }
                } catch (error) {
                    alert((error as Error).message)
                } finally {
                    setState({isLoading: false})
                }
            };
            fetchUser();
        }
    }, [userId]);


    useOnce(async () => {
        try {
            const response = await fetch(import.meta.env.VITE_ZTOR_URL + "/user-info", {
                credentials: "include",
            })

            if (response.status === 401) {
                redirectToLogin()
                return
            }
            if (response.status === 500) {
                return
            }

            if (response.ok) {
                const userInfo = await response.json();
                setUserId(userInfo.sub);

                setState((prevState) => ({
                    ...prevState,
                    isLoading: false,
                    isLoggedIn: true,
                    username: userInfo.preferred_username,
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