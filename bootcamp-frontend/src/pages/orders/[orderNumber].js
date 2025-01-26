import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import axios from "axios";
import BootcampHeader from "@/components/header/header";
import styles from "@/styles/OrderDetailPage.module.css"; // Import modular styles

export default function OrderDetailPage() {
    const [order, setOrder] = useState(null);
    const [selectedStatus, setSelectedStatus] = useState(""); // State for selected status
    const router = useRouter();
    const { orderNumber } = router.query;

    const validStatuses = ["NEW", "IN_PROGRESS", "COMPLETED", "CANCELLED"]; // Valid order statuses

    const fetchOrder = async () => {
        try {
            const loginToken = localStorage.getItem("loginToken");
            if (!loginToken) {
                router.push("/login");
                return;
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };

            const response = await axios.get(`http://localhost:8080/orders/${orderNumber}`, { headers });
            setOrder(response.data);
            setSelectedStatus(response.data.orderStatus); // Initialize dropdown with current status
        } catch (error) {
            console.error("Error fetching order:", error);
        }
    };

    const updateOrderStatus = async (newStatus) => {
        try {
            const loginToken = localStorage.getItem("loginToken");
            if (!loginToken) {
                router.push("/login");
                return;
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };

            const requestBody = { orderStatus: newStatus };
            const response = await axios.put(`http://localhost:8080/orders/${orderNumber}`, requestBody, { headers });

            setOrder(response.data); // Update order details with the response
            setSelectedStatus(newStatus); // Reflect the new status in the dropdown
            alert("Order status updated successfully!");
        } catch (error) {
            console.error("Error updating order status:", error);
            alert("Failed to update order status.");
        }
    };

    const handleStatusChange = (event) => {
        const newStatus = event.target.value;
        updateOrderStatus(newStatus);
    };

    useEffect(() => {
        if (orderNumber) {
            fetchOrder();
        }
    }, [orderNumber]);

    const handleBackButtonClick = () => {
        router.push("/orders"); // Navigate back to the orders page
    };

    if (!order) {
        return <div>Loading...</div>;
    }

    return (
        <div className="page-container">
            <BootcampHeader />
            <div className="back-button-container">
                <button className="back-button" onClick={handleBackButtonClick}>
                    Back to Orders
                </button>
            </div>
            <main className="main-content">
                <div className={styles.orderDetailsContainer}>
                    {/* Left Section: Order Details */}
                    <div className={styles.orderDetailsSection}>
                        <h2>Order Details</h2>
                        <p>Order Number: {order.orderNumber}</p>
                        <p>Status: {order.orderStatus}</p>
                        <p>Create At: {order.createAt}</p>
                        <p>Update At: {order.updateAt}</p>
                        <p>
                            User: {order.user?.name || "N/A"} ({order.user?.email || "N/A"})
                        </p>
                    </div>

                    {/* Center Section: Products */}
                    <div className={styles.orderProductsSection}>
                        <h2>Products</h2>
                        {order.productCarts.map((productCart, index) => (
                            <p key={index}>
                                {productCart.product.name} - {productCart.product.price} USD
                                <br />
                                ({productCart.product.description}, SKU: {productCart.product.sku}, Quantity:{" "}
                                {productCart.quantity})
                            </p>
                        ))}
                    </div>

                    {/* Right Section: Update Order Status */}
                    <div className={styles.orderStatusSection}>
                        <h2>Update Order Status</h2>
                        <select value={selectedStatus} onChange={handleStatusChange}>
                            {validStatuses.map((status) => (
                                <option key={status} value={status}>
                                    {status}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>
            </main>
            <footer className="footer">&copy; 2025 The Lotus Team. All rights reserved.</footer>
        </div>
    );
}
