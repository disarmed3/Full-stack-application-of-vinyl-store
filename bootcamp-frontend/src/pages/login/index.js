// import styles from '@/pages/login/Login.module.css';

import axios from "axios";
import {router} from "next/client";

export default function LoginPage() {



    const handleLogin = async () => {
        // get value from input email and password

        let email = document.getElementById('formLoginEmailInputField').value;
        let password = document.getElementById('formLoginPasswordInputField').value;

        // Differentiate between new and existing product
        const authHeader = `Basic ${btoa(`${email}:${password}`)}`;
        const headers = {
            Authorization: authHeader,
            'Content-Type': 'application/json',
        };

        try {
            // Send login request
            const loginResp = await axios.post('http://localhost:8080/login', {}, { headers });

            // Log the response to check if it includes role and email
            console.log(loginResp.data);

            // Retrieve user details from the response
            const { email, role } = loginResp.data;

            // Encode login credentials as token
            const loginToken = btoa(`${email}:${password}`);

            // Store login information in localStorage
            localStorage.setItem('loginToken', loginToken); // Token for API calls
            localStorage.setItem('userRole', role);         // User role (ROLE_USER or ROLE_ADMIN)
            localStorage.setItem('userEmail', email);       // User email for personalized access

            // go to /products
            await router.push('/products');

        } catch (error) {
            console.error("Error logging in:", error);
            alert("Failed to log in. Please check your credentials.");
            window.location.reload();
        }


    }


    return (
        <section className="h-100 gradient-form" style={{ backgroundColor: '#eee' }}>
            <div className="container py-5 h-100">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-xl-10">
                        <div className="card rounded-3 text-black">
                            <div className="row g-0">
                                <div className="col-lg-6">
                                    <div className="card-body p-md-5 mx-md-4">

                                        <div className="text-center">
                                            <img
                                                src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/lotus.webp"
                                                style={{ width: '185px' }}
                                                alt="logo"
                                            />
                                            <h4 className="mt-1 mb-5 pb-1">We are The Lotus Team</h4>
                                        </div>

                                        <form>
                                            <p>Please login to your account</p>

                                            <div data-mdb-input-init className="form-outline mb-4">
                                                <input
                                                    type="email"
                                                    id="formLoginEmailInputField"
                                                    className="form-control"
                                                    placeholder="email address"
                                                />
                                                <label className="form-label" htmlFor="form2Example11">Username</label>
                                            </div>

                                            <div data-mdb-input-init className="form-outline mb-4">
                                                <input
                                                    type="password"
                                                    id="formLoginPasswordInputField"
                                                    className="form-control"
                                                />
                                                <label className="form-label" htmlFor="form2Example22">Password</label>
                                            </div>

                                            <div className="text-center pt-1 mb-5 pb-1">
                                                <button
                                                    data-mdb-button-init
                                                    data-mdb-ripple-init
                                                    className="btn btn-primary btn-block fa-lg gradient-custom-2 mb-3"
                                                    type="button"
                                                    onClick={handleLogin}
                                                >
                                                    Log in
                                                </button>
                                                <a className="text-muted" href="#!">Forgot password?</a>
                                            </div>

                                            <div className="d-flex align-items-center justify-content-center pb-4">
                                                <p className="mb-0 me-2">Don't have an account?</p>
                                                <button
                                                    type="button"
                                                    data-mdb-button-init
                                                    data-mdb-ripple-init
                                                    className="btn btn-outline-danger"
                                                >
                                                    Create new
                                                </button>
                                            </div>
                                        </form>

                                    </div>
                                </div>
                                <div className="col-lg-6 d-flex align-items-center gradient-custom-2">
                                    <div className="text-white px-3 py-4 p-md-5 mx-md-4">
                                        <h4 className="mb-4">We are more than just a vinyl e-shop</h4>
                                        <p className="small mb-0">
                                            Welcome to Lotus Team Vinyl! Immerse yourself in the golden age of music with our exquisite collection. Rediscover timeless classics and explore hidden gems. Let the needle drop and experience the warmth and nostalgia only vinyl can provide.
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}