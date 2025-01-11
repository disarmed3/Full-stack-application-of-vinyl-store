import {useEffect, useState} from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";
import {router} from "next/client";


export default function HomePage() {

    const [users, setUsers] = useState([]);

    const fetchUsers = async () => {
        try {
            const loginToken = localStorage.getItem('loginToken');
            const userRole = localStorage.getItem('userRole'); // Get user role

            if (!loginToken) {
                router.push('/login');
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };

            let response;
            if (userRole === 'ROLE_ADMIN') {
                // Fetch all users if role is admin
                response = await axios.get("http://localhost:8080/users", { headers });
                setUsers(response.data); // Expecting an array
            } else {
                // Fetch only the logged-in user's details if role is user
                const userEmail = localStorage.getItem('userEmail');
                response = await axios.get(`http://localhost:8080/users/${userEmail}`, { headers });
                setUsers([response.data]); // Wrap the single user in an array
            }



        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };


    // Fetch users on page load
    useEffect(() => {
        fetchUsers();
    }, []);



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
                                <h3>{user.name}</h3>
                                <p>{user.email}</p>
                                <p>Password: {user.password}</p>
                                <p>Phone: {user.phoneNumber}</p>
                                <p>Address: {user.address}</p>
                            </div>
                        ))
                    )}
                </div>
            </main>
            <footer className="footer">
                &copy; 2024 The Lotus Team. All rights reserved.
            </footer>
        </div>
    );
}