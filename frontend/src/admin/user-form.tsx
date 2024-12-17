import React, { FormEvent, FunctionComponent, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { PrimeReactProvider } from "primereact/api";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { User } from "zero-zummon"; // Assuming this is the user model
import { redirectToLogin } from "./use-users";

export const UserForm: FunctionComponent = () => {
    const {userId} = useParams<{ userId: string }>();
    const [user, setUser] = useState<User | null>(null);
    const [originalData, setOriginalData] = useState<User | null>(null);

    const [loading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();

    const handleCancel = () => {
        if (originalData) {
            setUser(originalData); // Revert to original data
        }
        setIsEditing(false);
    };

    const handleEditToggle = () => {
        setIsEditing(true);
    };

    const handleInputChange =(e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setUser((prev) => ({ ...prev, [name]: value } as User));
    };

    useEffect(() => {
        if (userId) {
            const fetchUser = async () => {
                setLoading(true);
                try {
                    const response = await fetch(`${import.meta.env.VITE_ZTOR_URL}/users/${userId}`, {
                        credentials: "include",
                    });
                    if (response.status === 401) {
                        redirectToLogin();
                        return;
                    }
                    if (response.ok) {
                        const userData = await response.json();
                        setUser(userData);
                        setOriginalData(userData);
                    } else {
                        alert(`Error fetching user: ${response.statusText}`);
                    }
                } catch (error) {
                    alert((error as Error).message);
                } finally {
                    setLoading(false);
                }
            };
            fetchUser();
        } else {
            setIsEditing(true);
        }
    }, [userId]);

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const method = userId ? "PUT" : "POST";
            const url = userId
                ? `${import.meta.env.VITE_ZTOR_URL}/users/${userId}`
                : `${import.meta.env.VITE_ZTOR_URL}/users`;
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(user),
            });
            if (response.status === 401) {
                redirectToLogin();
                return;
            }
            if (response.ok) {
                alert(`User ${userId ? "updated" : "created"} successfully!`);
                navigate(`/users/${userId}`);
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message}`);
            }
        } catch (error) {
            alert((error as Error).message);
        } finally {
            setIsEditing(false);
            setLoading(false);
        }
    };

    return (
        <PrimeReactProvider>
            <div style={{ padding: "20px", maxWidth: "500px", margin: "0 auto" }}>
                <h3>{userId ? "Edit User" : "Add User"}</h3>
                <form
                    onSubmit={handleSubmit}
                    style={{ display: "flex", flexDirection: "column", gap: "10px" }}
                >
                    <label htmlFor="note">Note:</label>
                    <InputText
                        id="note"
                        name="note"
                        value={user?.note}
                        defaultValue={user?.note || ""}
                        onChange={handleInputChange}
                        disabled={!isEditing}
                    />

                    <div style={{ display: "flex", justifyContent: "space-between", marginTop: "10px" }}>
                        {isEditing ? (
                            <>
                                <Button label="Cancel" onClick={handleCancel} type="button" disabled={loading} />
                                <Button label={loading ? "Saving..." : "Save"} type="submit" disabled={loading} />
                            </>
                        ) : (
                            <Button label="Edit" onClick={handleEditToggle} type="button" disabled={loading} />
                        )}
                    </div>
                </form>
            </div>
        </PrimeReactProvider>
    );
};
