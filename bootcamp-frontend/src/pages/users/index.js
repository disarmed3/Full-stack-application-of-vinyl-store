import { useEffect, useState } from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";
import Link from "next/link";
import { useRouter } from "next/router";

export default function UsersPage() {
    const router = useRouter();
    const [users, setUsers] = useState([]); // Initialize with empty array
    const [userRole, setUserRole] = useState("");
    const [editingUser, setEditingUser] = useState(null);
    const [editedUser, setEditedUser] = useState({});

    const fetchUsers = async () => {
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

            const response = await axios.get("http://localhost:8080/users", { headers });
            setUsers(response.data);
        } catch (error) {
            console.error("Error fetching users:", error);
            // Handle error - you might want to show an error message to the user
            setUsers([]); // Ensure users is an empty array if the request fails
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleDeleteUser = async (email) => {
        const confirmDelete = window.confirm("Are you sure?");
        if (confirmDelete) {
            try {
                const loginToken = localStorage.getItem("loginToken");
                const authHeader = `Basic ${loginToken}`;
                const headers = { Authorization: authHeader };

                await axios.delete(`http://localhost:8080/users/${email}`, { headers });

                localStorage.clear();
                window.location.href = "http://localhost:3000/login";
            } catch (error) {
                console.error("Error deleting user:", error);
                // Handle error - you might want to show an error message to the user
            }
        }
    };

    const handleEditUser = (user) => {
        setEditingUser(user.email);
        setEditedUser(user);
    };

    const handleCancelEdit = () => {
        setEditingUser(null);
    };

    const handleUpdateUser = async () => {
        try {
            const loginToken = localStorage.getItem("loginToken");
            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };

            await axios.put("http://localhost:8080/users", editedUser, { headers });

            fetchUsers();
            setEditingUser(null);
        } catch (error) {
            console.error("Error updating user:", error);
            // Handle error - you might want to show an error message to the user
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
                <h1>Users</h1>
                <div className="product-grid">
                    {users.length === 0 ? (
                        <p>No users found</p>
                    ) : (
                        users.map((user) => (
                            <div key={user.id} className="product-card">
                                {userRole === "ROLE_ADMIN" ? (
                                    <Link href={`/users/details?email=${encodeURIComponent(user.email)}`}>
                                        <h3 style={{ cursor: "pointer", color: "blue", textDecoration: "underline" }}>
                                            {user.name}
                                        </h3>
                                    </Link>
                                ) : (
                                    <h3>{user.name}</h3>
                                )}
                                {editingUser === user.email ? (
                                    <>
                                        <p>
                                            Name*:{" "}
                                            <input
                                                type="text"
                                                value={editedUser.name}
                                                onChange={(e) => handleInputChange(e, "name")}
                                            />
                                        </p>
                                        <p>
                                            Email: {editedUser.email}
                                        </p>
                                        <p>
                                            Password*:{" "}
                                            <input
                                                type="text"
                                                value={editedUser.password}
                                                onChange={(e) => handleInputChange(e, "password")}
                                            />
                                        </p>
                                        <p>
                                            Phone*:{" "}
                                            <input
                                                type="text"
                                                value={editedUser.phoneNumber}
                                                onChange={(e) => handleInputChange(e, "phoneNumber")}
                                            />
                                        </p>
                                        <p>
                                            Address*:{" "}
                                            <input
                                                type="text"
                                                value={editedUser.address}
                                                onChange={(e) => handleInputChange(e, "address")}
                                            />
                                        </p>
                                        <div className="button-container">
                                            <button onClick={handleCancelEdit}>Cancel</button>
                                            <button onClick={handleUpdateUser}>Update</button>
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <p>{user.email}</p>
                                        <p>Password: {user.password}</p>
                                        <p>Phone: {user.phoneNumber}</p>
                                        <p>Address: {user.address}</p>
                                        {userRole === "ROLE_USER" && (
                                            <div className="button-container">
                                                <button onClick={() => handleEditUser(user)}>Edit User</button>
                                                <button onClick={() => handleDeleteUser(user.email)}>Delete User</button>
                                            </div>
                                        )}
                                    </>
                                )}
                            </div>
                        ))
                    )}
                </div>
            </main>
            <footer className="footer">
                &copy; 2025 The Lotus Team. All rights reserved.
            </footer>
        </div>
    );
}