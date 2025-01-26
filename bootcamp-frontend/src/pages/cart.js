import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import BootcampHeader from "@/components/header/header";



export default function ShoppingCartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [cartCount, setCartCount] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);
    const [userRole, setUserRole] = useState(null); // To check for ROLE_USER
    const [userEmail, setUserEmail] = useState(null); // Logged-in user's email
    const router = useRouter();

    useEffect(() => {
        // Retrieve cart, user role, and email from localStorage
        const storedCart = JSON.parse(localStorage.getItem("cartItems")) || [];
        const storedCartCount = localStorage.getItem("cartCount") || 0;
        const storedUserRole = localStorage.getItem("userRole");
        const storedUserEmail = localStorage.getItem("userEmail");

        setCartItems(storedCart);
        setCartCount(parseInt(storedCartCount, 10));
        setUserRole(storedUserRole);
        setUserEmail(storedUserEmail);
        calculateTotalPrice(storedCart);
    }, []);

    const calculateTotalPrice = (items) => {
        const total = items.reduce((sum, item) => sum + parseFloat(item.price), 0);
        setTotalPrice(total.toFixed(2)); // Round to 2 decimal places
    };

    const handleRemoveItem = (index) => {
        const updatedCart = [...cartItems];
        updatedCart.splice(index, 1);

        setCartItems(updatedCart);
        localStorage.setItem("cartItems", JSON.stringify(updatedCart));

        const updatedCount = updatedCart.length;
        setCartCount(updatedCount);
        localStorage.setItem("cartCount", updatedCount);

        calculateTotalPrice(updatedCart);

        const headerCartCount = document.querySelector(".cart-count");
        if (headerCartCount) {
            headerCartCount.textContent = `(${updatedCount})`;
        }
    };

    const handlePlaceOrder = async () => {
        if (!userEmail || cartItems.length === 0) {
            alert("You need to log in or add items to your cart before placing an order.");
            return;
        }

        const payload = {
            user: {
                email: userEmail,
            },
            productCarts: cartItems.map((item) => ({
                product: {
                    sku: item.sku,
                },
                quantity: 1.0, // Assuming a quantity of 1 for each cart item
            })),
        };

        // Retrieve loginToken from localStorage
        const loginToken = localStorage.getItem("loginToken");
        if (!loginToken) {
            alert("Authorization token is missing. Please log in again.");
            return;
        }

        const headers = {
            Authorization: `Basic ${loginToken}`,
            "Content-Type": "application/json",
        };

        try {
            const response = await fetch("http://localhost:8080/orders/user", {
                method: "POST",
                headers: headers,
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert("Order placed successfully!");
                setCartItems([]);
                setCartCount(0);
                setTotalPrice(0);
                localStorage.removeItem("cartItems");
                localStorage.setItem("cartCount", 0);

                // Update cart count in the header dynamically
                const headerCartCount = document.querySelector(".cart-count");
                if (headerCartCount) {
                    headerCartCount.textContent = `(0)`;
                }

                router.push("/orders"); // Redirect to /orders page
            } else {
                const errorData = await response.json();
                alert(`Failed to place the order: ${errorData.message}`);
            }
        } catch (error) {
            console.error("Error placing order:", error);
            alert("An error occurred while placing the order.");
        }
    };


    return (
        <div className="page-container">
            <BootcampHeader />
            <main className="main-content">
                <h1>Shopping Cart</h1>
                {cartItems.length > 0 ? (
                    <>
                        <div className="cart-list">
                            {cartItems.map((item, index) => (
                                <div key={index} className="cart-item">
                                    <div>
                                        <h3>{item.name}</h3>
                                        <p>Description: {item.description}</p>
                                        <p>SKU: {item.sku}</p>
                                        <p>Price: ${item.price}</p>
                                    </div>
                                    <button
                                        className="remove-button"
                                        onClick={() => handleRemoveItem(index)}
                                    >
                                        Remove
                                    </button>
                                </div>
                            ))}
                        </div>
                        <div className="total-price-box">
                            <h2>Total Price: ${totalPrice}</h2>
                            {userRole === "ROLE_USER" && (
                                <button className="order-button" onClick={handlePlaceOrder}>
                                    Place the Order
                                </button>
                            )}
                        </div>
                    </>
                ) : (
                    <p>Your cart is empty!</p>
                )}
            </main>
            <footer className="footer">&copy; 2025 The Lotus Team. All rights reserved.</footer>
            <style jsx>{`
                .cart-list {
                    display: flex;
                    flex-direction: column;
                    gap: 16px;
                }
                .cart-item {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    padding: 16px;
                    width: 100%; /* Ensure it takes full width */
                }
                .cart-item div {
                    width: 66%; /* Adjust width for product details */
                }
                .remove-button {
                    background-color: #ff4500;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    padding: 8px 16px;
                    cursor: pointer;
                }
                .total-price-box {
                    margin-top: 16px;
                    padding: 16px;
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    background-color: #f9f9f9;
                }
                .order-button {
                    margin-top: 16px;
                    width: 100%;
                    padding: 10px;
                    background-color: #007bff;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-size: 1em;
                }
                .order-button:hover {
                    background-color: #0056b3;
                }
                .total-price-box h2 {
                    margin: 0;
                    font-size: 1.5em;
                    color: #333;
                }
            `}</style>
        </div>
    );
}
