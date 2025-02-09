import axios from "axios";
import { useRouter } from "next/router";

export default function LoginPage() {
    const router = useRouter();

    const handleLogin = async () => {
        let email = document.getElementById('formLoginEmailInputField').value;
        let password = document.getElementById('formLoginPasswordInputField').value;

        const authHeader = `Basic ${btoa(`${email}:${password}`)}`;
        const headers = {
            Authorization: authHeader,
            'Content-Type': 'application/json',
        };

        try {
            const loginResp = await axios.post('http://localhost:8080/login', {}, { headers });

            console.log(loginResp.data);

            const { email, role } = loginResp.data;
            const loginToken = btoa(`${email}:${password}`);

            localStorage.setItem('loginToken', loginToken);
            localStorage.setItem('userRole', role);
            localStorage.setItem('userEmail', email);

            await router.push('/products');
        } catch (error) {
            console.error("Error logging in:", error);
            alert("Failed to log in. Please check your credentials.");
            window.location.reload();
        }
    };

    const handleNavigateToRegister = () => {
        router.push('/register');
    };

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

                                            <div className="form-outline mb-4">
                                                <input
                                                    type="email"
                                                    id="formLoginEmailInputField"
                                                    className="form-control"
                                                    placeholder="email address"
                                                />
                                                <label className="form-label" htmlFor="formLoginEmailInputField">Username</label>
                                            </div>

                                            <div className="form-outline mb-4">
                                                <input
                                                    type="password"
                                                    id="formLoginPasswordInputField"
                                                    className="form-control"
                                                />
                                                <label className="form-label" htmlFor="formLoginPasswordInputField">Password</label>
                                            </div>

                                            <div className="text-center pt-1 mb-5 pb-1">
                                                <button
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
                                                    className="btn btn-outline-danger"
                                                    onClick={handleNavigateToRegister}
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
    );
}
