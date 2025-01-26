import { useEffect, useState } from "react";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/router";
import BootcampHeader from "@/components/header/header";

export default function HomePage() {
    const [products, setProducts] = useState([]);
    const [userRole, setUserRole] = useState(null);
    const router = useRouter();

    const fetchProducts = async () => {
        try {
            const loginToken = localStorage.getItem('loginToken');
            if (!loginToken) {
                router.push('/login');
                return;
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = { Authorization: authHeader };
            const response = await axios.get("http://localhost:8080/products", { headers });
            setProducts(response.data);

            // Fetch and set the user's role from localStorage
            const role = localStorage.getItem('userRole');
            setUserRole(role);
        } catch (error) {
            console.error("Error fetching products:", error);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    return (
        <div className="page-container">
            <BootcampHeader />
            <main className="main-content">
                {/* Conditionally render Add Product button for ROLE_ADMIN users */}
                {userRole === 'ROLE_ADMIN' && (
                    <div className="add-product-container">
                        <Link href="/products/new">
                            <button className="add-product-button">Add a Product</button>
                        </Link>
                    </div>
                )}

                <h1>Products</h1>
                <div className="product-grid">
                    {products.map((product) => (
                        <div key={product.id} className="product-card">
                            <Link href={`/products/${product.sku}`}>
                                <h3>{product.name}</h3>
                            </Link>
                            <p>{product.description}</p>
                            <p>Stock: {product.stock}</p>
                            <p>Price: ${product.price}</p>
                        </div>
                    ))}
                </div>
            </main>
            <footer className="footer">
                &copy; 2025 The Lotus Team. All rights reserved.
            </footer>
        </div>
    );
}
