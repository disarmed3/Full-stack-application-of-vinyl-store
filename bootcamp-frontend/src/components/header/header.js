import { useRouter } from "next/router";
import { useState, useEffect } from "react";

export default function BootcampHeader() {
    const router = useRouter();
    const [cartCount, setCartCount] = useState(0); // Counter for shopping cart items

    useEffect(() => {
        // Initialize cart count from localStorage or set it to 0
        const storedCartCount = localStorage.getItem("cartCount");
        setCartCount(storedCartCount ? parseInt(storedCartCount, 10) : 0);
    }, []);

    const handleLogout = () => {
        localStorage.clear(); // Clears all items from local storage
        router.push("/login"); // Redirect to the login page
    };

    return (
        <header className="header">
            <div className="header-logo">The Lotus Team</div>
            <nav className="header-menu">
                <a href="/cart" className="header-menu-item">
                    Shopping Cart <span className="cart-count">({cartCount})</span>
                </a>
                <a href="/products" className="header-menu-item">Products</a>
                <a href="/orders" className="header-menu-item">Orders</a>
                <a href="/users" className="header-menu-item">Users</a>
                <a
                    className="header-menu-item logout-link"
                    onClick={handleLogout}
                >
                    Logout
                </a>
            </nav>
            <style jsx>{`
                .header-menu-item {
                    cursor: pointer;
                }
                .cart-count {
                    font-weight: bold;
                    color: #ff4500;
                }
            `}</style>
        </header>
    );
}
