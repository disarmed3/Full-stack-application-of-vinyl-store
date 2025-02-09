import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";

export default function UserDetailsPage() {
    const router = useRouter();
    const { email } = router.query; // Get the email from query parameters
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (!email) return; // Wait until the email is available

        const fetchUserDetails = async () => {
            try {
                const loginToken = localStorage.getItem("loginToken");
                if (!loginToken) {
                    router.push("/login");
                    return;
                }
                const authHeader = `Basic ${loginToken}`;
                const headers = { Authorization: authHeader };

                // Call backend endpoint to fetch user details for the given email
                const response = await axios.get(`http://localhost:8080/users/${encodeURIComponent(email)}`, { headers });
                setUser(response.data);
            } catch (error) {
                console.error("Error fetching user details:", error);
            }
        };

        fetchUserDetails();
    }, [email, router]);

    return (
        <div className="page-container">
            <BootcampHeader />
            <main className="main-content">
                <h1>User Details</h1>
                {user ? (
                    <div className="product-card">
                        <h3>{user.name}</h3>
                        <p>{user.email}</p>
                        <p>Password: {user.password}</p>
                        <p>Phone: {user.phoneNumber}</p>
                        <p>Address: {user.address}</p>
                    </div>
                ) : (
                    <p>Loading user details...</p>
                )}
            </main>
            <footer className="footer">
                &copy; 2025 The Lotus Team. All rights reserved.
            </footer>
        </div>
    );
}
