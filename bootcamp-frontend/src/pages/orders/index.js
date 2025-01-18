import { useEffect, useState } from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";
import { useRouter } from "next/router"; // Adjusted to use Next.js router

export default function HomePage() {
    const [orders, setOrders] = useState([]);
    const [userRole, setUserRole] = useState(null); // State to store user role
    const router = useRouter(); // Use Next.js router

    const fetchOrders = async () => {
        try {
            const loginToken = localStorage.getItem("loginToken");
            const role = localStorage.getItem("userRole");
            setUserRole(role); // Set user role for conditional rendering

            if (!loginToken) {
                router.push("/login");
                return;
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };

            let response;
            if (role === "ROLE_ADMIN") {
                // Fetch all orders for admin
                response = await axios.get("http://localhost:8080/orders", { headers });
                setOrders(response.data); // Expecting an array
            } else {
                // Fetch only the logged-in user's orders
                const userEmail = localStorage.getItem("userEmail");
                response = await axios.get(`http://localhost:8080/orders/user/${userEmail}`, { headers });
                setOrders(response.data)
            }
        } catch (error) {
            console.error("Error fetching orders:", error);
        }
    };

    // Fetch orders on page load
    useEffect(() => {
        fetchOrders();
    }, []);

    return (
        <div className="page-container">
            <BootcampHeader />
            <main className="main-content">
                <h1>Orders</h1>
                <div className="product-grid">
                    {console.log("Orders:", orders)}
                    {userRole === "ROLE_ADMIN" ? (
                        // Render UI for ROLE_ADMIN
                        orders.map((order) => (
                            <div key={order.orderNumber} className="product-card">
                                <h3>{order.orderStatus}</h3>
                                <p>Order Number: {order.orderNumber}</p>
                                <p>Create At: {order.createAt}</p>
                                <p>Update At: {order.updateAt}</p>
                                <p>User: {order.user?.name || "N/A"} ({order.user?.email || "N/A"})</p>
                                <p>
                                    Products:{" "}
                                    {order.productCarts.map(
                                        (productCart) =>
                                            `${productCart.product.name} - ${productCart.product.price} USD (${productCart.product.description}, SKU: ${productCart.product.sku}, Quantity: ${productCart.quantity})`
                                    ).join("; ")}
                                </p>
                            </div>
                        ))
                    ) : (
                        // Render UI for ROLE_USER
                        orders.map((order) => (
                            <div key={order.orderNumber} className="product-card">
                                <h3>{order.orderStatus}</h3>
                                <p>Order Number: {order.orderNumber}</p>
                                <p>Create At: {order.createAt}</p>
                                <p>User: {order.userName} ({order.userEmail})</p>
                                <p>
                                    Products:{" "}
                                    {order.productCarts.map(
                                        (productCart) =>
                                            `${productCart.name} - ${productCart.price} USD (${productCart.description}, SKU: ${productCart.sku}, Quantity: ${productCart.quantity})`
                                    ).join("; ")}
                                </p>
                            </div>
                        ))
                    )}
                </div>
            </main>
            <footer className="footer">&copy; 2024 The Lotus Team. All rights reserved.</footer>
        </div>
    );
}
