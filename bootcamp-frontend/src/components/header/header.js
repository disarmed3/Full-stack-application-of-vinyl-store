import { useRouter } from "next/router";

export default function BootcampHeader() {
    const router = useRouter();

    const handleLogout = () => {
        localStorage.removeItem("loginToken");
        router.push("/login");
    };

    return (
        <header className="header">
            <div className="header-logo">The Lotus Team</div>
            <nav className="header-menu">
                <a href="/products" className="header-menu-item">Products</a>
                <a href="/orders" className="header-menu-item">Orders</a>
                <a href="/users" className="header-menu-item">Users</a>
                {/* Adding pointer style to Logout */}
                <a
                    className="header-menu-item logout-link"
                    onClick={handleLogout}
                >
                    Logout
                </a>
            </nav>
            <style jsx>{`
                .header-menu-item {
                    cursor: pointer; /* Ensure all menu items have a pointer cursor */
                }
            `}</style>
        </header>
    );
}
