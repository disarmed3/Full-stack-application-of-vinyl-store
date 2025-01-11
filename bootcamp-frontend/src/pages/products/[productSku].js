import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import BootcampHeader from "@/components/header/header";

export default function ProductPage() {
    const router = useRouter();
    const { productSku } = router.query;

    const isNew = productSku === 'new';

    const [product, setProduct] = useState(null);
    const [editedProduct, setEditedProduct] = useState({
        name: '',
        description: '',
        stock: '',
        price: ''
    });
    const [editing, setEditing] = useState(false);
    const [userRole, setUserRole] = useState(null); // State for user role

    useEffect(() => {
        // Retrieve user role from localStorage
        const storedUserRole = localStorage.getItem('userRole');
        setUserRole(storedUserRole);

        if (!isNew && productSku) {
            fetchProductBySku(productSku);
        } else if (isNew) {
            setEditedProduct({
                name: '',
                description: '',
                stock: '',
                price: ''
            });
            setEditing(true);
            setProduct(null);
        }
    }, [productSku, isNew]);

    const fetchProductBySku = async (sku) => {
        if (!sku || sku === 'new') return;
        try {
            const loginToken = localStorage.getItem('loginToken');
            if (!loginToken) {
                router.push('/login');
            }

            const authHeader = `Basic ${loginToken}`;
            const headers = {
                Authorization: authHeader,
            };
            const response = await axios.get(`http://localhost:8080/products/${sku}`, { headers });
            setProduct(response.data);
            setEditedProduct(response.data);
            setEditing(false);
        } catch (error) {
            console.error("Error fetching product:", error);
        }
    };

    const handleEdit = () => {
        if (!product && !isNew) return;
        setEditedProduct(product ? { ...product } : { ...editedProduct });
        setEditing(true);
    };

    const handleDelete = async () => {
        const confirmed = window.confirm('Are you sure?');
        if (confirmed) {
            try {
                const loginToken = localStorage.getItem('loginToken');
                if (!loginToken) {
                    router.push('/login');
                }

                const authHeader = `Basic ${loginToken}`;
                const headers = {
                    Authorization: authHeader
                };

                await axios.delete(`http://localhost:8080/products/${product.sku}`, { headers });

                alert('Product deleted successfully!');
                router.push('/products'); // Redirect to products page
            } catch (error) {
                console.error('Error deleting product:', error);
                alert('Failed to delete product.');
            }
        }
    };

    const handleCancel = () => {
        if (isNew) {
            router.push('/products');
        } else {
            setEditedProduct({ ...product });
            setEditing(false);
        }
    };

    const validEditForm = () => {
        const { name, description, stock, price } = editedProduct;
        if (!name || name.trim() === '') {
            alert("Name cannot be empty");
            return false;
        }
        if (!description || description.trim() === '') {
            alert("Description cannot be empty");
            return false;
        }
        if (stock === '' || isNaN(stock) || Number(stock) < 0) {
            alert("Stock must be a number greater than or equal to 0");
            return false;
        }
        if (price === '' || isNaN(price) || Number(price) <= 0) {
            alert("Price must be a number greater than 0");
            return false;
        }
        return true;
    };

    const handleSave = async () => {
        if (!editedProduct) return;
        if (!validEditForm()) return;

        const loginToken = localStorage.getItem('loginToken');
        if (!loginToken) {
            router.push('/login');
        }
        const authHeader = `Basic ${loginToken}`;
        const headers = {
            Authorization: authHeader,
            'Content-Type': 'application/json',
        };

        try {
            if (isNew) {
                let newProductResp = await axios.post(`http://localhost:8080/products`, editedProduct, { headers });
                router.push(`/products/${newProductResp.data.sku}`);
            } else {
                await axios.put(`http://localhost:8080/products/${editedProduct.sku}`, editedProduct, { headers });
                window.location.reload();
            }
        } catch (error) {
            console.error("Error saving product:", error);
            alert("Failed to save product.");
            window.location.reload();
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedProduct(prev => ({ ...prev, [name]: value }));
    };

    return (
        <div className="page-container">
            <BootcampHeader />

            {/* Back Button under Logo */}
            <div className="back-button-container">
                <button
                    className="back-button"
                    onClick={() => router.push('/products')}
                >
                    &larr; Back to Products
                </button>
            </div>

            <main className="main-content">
                <h1>{isNew ? 'Create New Product' : 'Product'}</h1>

                {(!isNew && product) || isNew ? (
                    <div className="product-card">
                        {editing ? (
                            <>
                                <input
                                    type="text"
                                    name="name"
                                    value={editedProduct.name || ''}
                                    onChange={handleInputChange}
                                    placeholder="Name"
                                />
                                <textarea
                                    name="description"
                                    value={editedProduct.description || ''}
                                    onChange={handleInputChange}
                                    placeholder="Description"
                                />
                                <input
                                    type="number"
                                    name="stock"
                                    value={editedProduct.stock || ''}
                                    onChange={handleInputChange}
                                    placeholder="Stock"
                                />
                                <input
                                    type="number"
                                    name="price"
                                    value={editedProduct.price || ''}
                                    onChange={handleInputChange}
                                    placeholder="Price"
                                    step="0.01"
                                />
                                <div className="button-container">
                                    <button onClick={handleCancel}>Cancel</button>
                                    <button onClick={handleSave}>Save</button>
                                </div>
                            </>
                        ) : (
                            <>
                                {!isNew && product && (
                                    <>
                                        <h3>{product.name}</h3>
                                        <p>{product.description}</p>
                                        <p>Stock: {product.stock}</p>
                                        <p>Price: ${product.price}</p>
                                        {userRole === 'ROLE_ADMIN' && ( // Conditionally render buttons
                                            <div className="button-container">
                                                <button onClick={handleDelete} className="delete-button">Delete</button>
                                                <button onClick={handleEdit}>Edit</button>
                                            </div>
                                        )}
                                    </>
                                )}
                            </>
                        )}
                    </div>
                ) : (
                    <p>Loading...</p>
                )}
            </main>
            <footer className="footer">
                &copy; 2024 Your Company. All rights reserved.
            </footer>
        </div>
    );
}
