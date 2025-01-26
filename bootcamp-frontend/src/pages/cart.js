import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import BootcampHeader from "@/components/header/header"; // Import the shared header

export default function ShoppingCartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [cartCount, setCartCount] = useState(0);
    const router = useRouter();

    useEffect(() => {
        // Retrieve the cart from localStorage on page load
        const storedCart = JSON.parse(localStorage.getItem("cartItems")) || [];
        const storedCartCount = localStorage.getItem("cartCount") || 0;

        setCartItems(storedCart);
        setCartCount(parseInt(storedCartCount, 10));
    }, []);

    const handleRemoveItem = (sku) => {
        // Filter out the product to remove
        const updatedCart = cartItems.filter((item) => item.sku !== sku);

        // Update the cart in state and localStorage
        setCartItems(updatedCart);
        localStorage.setItem("cartItems", JSON.stringify(updatedCart));

        // Update the cart count
        const updatedCount = updatedCart.length;
        setCartCount(updatedCount);
        localStorage.setItem("cartCount", updatedCount);

        alert("Product removed from the cart!");
    };

    return (
        <div className="page-container">
            <BootcampHeader /> {/* Use the shared header */}
            <main className="main-content">
                <h1>Shopping Cart</h1>
                {cartItems.length > 0 ? (
                    <div className="cart-list">
                        {cartItems.map((item) => (
                            <div key={item.sku} className="cart-item">
                                <h3>{item.name}</h3>
                                <p>SKU: {item.sku}</p>
                                <p>Price: ${item.price}</p>
                                <button
                                    className="remove-button"
                                    onClick={() => handleRemoveItem(item.sku)}
                                >
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>Your cart is empty!</p>
                )}
            </main>
            <footer className="footer">&copy; 2024 The Lotus Team. All rights reserved.</footer>
            <style jsx>{`
                .cart-list {
                    display: flex;
                    flex-direction: column;
                    gap: 16px;
                }
                .cart-item {
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    padding: 16px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                .remove-button {
                    background-color: #ff4500;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    padding: 8px 16px;
                    cursor: pointer;
                }
            `}</style>
        </div>
    );
}
