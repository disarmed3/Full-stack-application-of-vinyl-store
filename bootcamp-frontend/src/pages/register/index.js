import React, { useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";

export default function RegisterPage() {
    const router = useRouter();
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        phoneNumber: "",
        address: "",
    });
    const [errorMessage, setErrorMessage] = useState("");

    const isFormValid = Object.values(formData).every(
        (field) => field.trim() !== ""
    );

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRegister = async () => {
        try {
            // Clear any previous error
            setErrorMessage("");
            await axios.post("http://localhost:8080/users/register", formData);
            alert("Registration successful! Redirecting to login...");
            router.push("/login");
        } catch (error) {
            console.error("Registration failed:", error);

            // Since the backend returns a 401 for an existing email without a message,
            // check for the 401 status code and set the error message accordingly.
            if (error.response && error.response.status === 401) {
                setErrorMessage(
                    "An account with this email already exists. Please use a different email."
                );
            } else {
                setErrorMessage("Registration failed. Please try again.");
            }
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.formWrapper}>
                <h2 style={styles.title}>Create Your Account</h2>

                {/* Display error message if one exists */}
                {errorMessage && <div style={styles.error}>{errorMessage}</div>}

                <input
                    type="text"
                    name="name"
                    placeholder="Full Name"
                    value={formData.name}
                    onChange={handleChange}
                    style={styles.input}
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email Address"
                    value={formData.email}
                    onChange={handleChange}
                    style={styles.input}
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    style={styles.input}
                />
                <input
                    type="tel"
                    name="phoneNumber"
                    placeholder="Phone Number"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    style={styles.input}
                />
                <input
                    type="text"
                    name="address"
                    placeholder="Address"
                    value={formData.address}
                    onChange={handleChange}
                    style={styles.input}
                />

                <button
                    onClick={handleRegister}
                    disabled={!isFormValid}
                    style={{
                        ...styles.button,
                        backgroundColor: isFormValid ? "#6a0dad" : "#cccccc",
                    }}
                >
                    Finish Registration
                </button>
            </div>
        </div>
    );
}

const styles = {
    container: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        backgroundColor: "#e6e0f8", // Pale purple
    },
    formWrapper: {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: "30px",
        borderRadius: "10px",
        backgroundColor: "white",
        boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)",
        width: "350px",
    },
    title: {
        fontSize: "24px",
        fontWeight: "bold",
        marginBottom: "20px",
        color: "#4b0082", // Dark purple for contrast
    },
    input: {
        width: "100%",
        padding: "10px",
        margin: "10px 0",
        borderRadius: "5px",
        border: "1px solid #ccc",
        fontSize: "16px",
    },
    button: {
        width: "100%",
        padding: "10px",
        marginTop: "15px",
        borderRadius: "5px",
        border: "none",
        color: "white",
        fontSize: "16px",
        cursor: "pointer",
    },
    error: {
        color: "red",
        marginBottom: "10px",
    },
};
