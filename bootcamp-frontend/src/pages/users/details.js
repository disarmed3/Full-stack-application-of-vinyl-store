import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";

export default function UserDetailsPage() {
    const router = useRouter();
    const { email } = router.query;
    const [user, setUser] = useState(null);
    const [userRole, setUserRole] = useState("");
    const [editingUser, setEditingUser] = useState(false);
    const [editedUser, setEditedUser] = useState({});

    useEffect(() => {
        if (!email) return;

        const fetchUserDetails = async () => {
            try {
                const loginToken = localStorage.getItem("loginToken");
                const storedUserRole = localStorage.getItem("userRole");
                setUserRole(storedUserRole);

                if (!loginToken) {
                    router.push("/login");
                    return;
                }
                const authHeader = `Basic ${loginToken}`;
                const headers = { Authorization: authHeader };

                const response = await axios.get(`http://localhost:8080/users/${encodeURIComponent(email)}`, { headers });
                setUser(response.data);
                setEditedUser(response.data);
            } catch (error) {
                console.error("Error fetching user details:", error);
            }
        };

        fetchUserDetails();
    }, [email, router]);

    const handleEditUser = () => {
        setEditingUser(true);
    };

    const handleCancelEdit = () => {
        setEditingUser(false);
    };

    const handleUpdateUser = async () => {
        try {
            const loginToken = localStorage.getItem("loginToken");
            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader, 'Content-Type': 'application/json' };

            const userUpdatePayload = {
                name: editedUser.name,
                email: editedUser.email,
                password: editedUser.password,
                phoneNumber: editedUser.phoneNumber,
                address: editedUser.address
            };

            await axios.put("http://localhost:8080/users", userUpdatePayload, { headers });
            setUser(editedUser);
            setEditingUser(false);
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };

    const handleDeleteUser = async () => {
        const confirmDelete = window.confirm("This will delete all associated orders too. Are you sure?");
        if (confirmDelete) {
            try {
                const loginToken = localStorage.getItem("loginToken");
                const authHeader = `Basic ${loginToken}`;
                const headers = { Authorization: authHeader };

                await axios.delete(`http://localhost:8080/users/${email}`, { headers });
                router.push("/users");
            } catch (error) {
                console.error("Error deleting user:", error);
            }
        }
    };

    const handleInputChange = (e, field) => {
        setEditedUser({
            ...editedUser,
            [field]: e.target.value,
        });
    };

    return (
        <div className="page-container">
            <BootcampHeader />
            <main className="main-content">
                <h1>User Details</h1>
                {user ? (
                    <div className="product-card">
                        {editingUser ? (
                            <>
                                <p>
                                    Name*: <input type="text" value={editedUser.name} onChange={(e) => handleInputChange(e, "name")} />
                                </p>
                                <p>Email: {editedUser.email}</p>
                                <p>
                                    Password*: <input type="text" value={editedUser.password} onChange={(e) => handleInputChange(e, "password")} />
                                </p>
                                <p>
                                    Phone*: <input type="text" value={editedUser.phoneNumber} onChange={(e) => handleInputChange(e, "phoneNumber")} />
                                </p>
                                <p>
                                    Address*: <input type="text" value={editedUser.address} onChange={(e) => handleInputChange(e, "address")} />
                                </p>
                                <div className="button-container">
                                    <button onClick={handleCancelEdit}>Cancel</button>
                                    <button onClick={handleUpdateUser}>Update</button>
                                </div>
                            </>
                        ) : (
                            <>
                                <h3>{user.name}</h3>
                                <p>{user.email}</p>
                                <p>Password: {user.password}</p>
                                <p>Phone: {user.phoneNumber}</p>
                                <p>Address: {user.address}</p>
                                {userRole === "ROLE_ADMIN" && (
                                    <div className="button-container">
                                        <button onClick={handleEditUser}>Edit User</button>
                                        <button onClick={handleDeleteUser}>Delete User</button>
                                    </div>
                                )}
                            </>
                        )}
                    </div>
                ) : (
                    <p>Loading user details...</p>
                )}
            </main>
            <footer className="footer">&copy; 2025 The Lotus Team. All rights reserved.</footer>
        </div>
    );
}
