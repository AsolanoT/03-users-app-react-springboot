import { NavLink } from 'react-router-dom';
import './navbar.css';
import { useContext } from 'react';
import { AuthContext } from '../../auth/context/AuthContext';

export const Navbar = () => {
    
    const { login, handlerLogout } = useContext(AuthContext);
    
    return (
        <nav className="navbar navbar-expand-lg navbar-dark custom-navbar">
            <div className="container-fluid px-4">
                <a className="navbar-brand-custom" href="#">
                    ⚡ UsersApp
                </a>

                <button 
                    className="navbar-toggler navbar-toggler-custom" 
                    type="button" 
                    data-bs-toggle="collapse" 
                    data-bs-target="#navbarNav" 
                    aria-controls="navbarNav" 
                    aria-expanded="false" 
                    aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/users">
                            Usuarios
                            </NavLink>
                        </li>

                        <li className="nav-item">
                            <NavLink className="nav-link" to="/users/register">
                            Registrar Usuarios
                            </NavLink>
                        </li>

                        
                    </ul>
                </div>

                <div className="collapse navbar-collapse justify-content-end" id="navbarNavLogout">
                    <div className="user-container">
                        {/* Badge de usuario */}
                        <div className="user-badge">
                            <div className="user-avatar">
                                {login.user?.username?.charAt(0).toUpperCase() || 'U'}
                            </div>
                            <span className="user-name">
                                {login.user?.username || 'Usuario'}
                            </span>
                        </div>

                        {/* Botón Logout */}
                        <button
                            onClick={handlerLogout}
                            className="btn-logout">
                            <span>🚪</span>
                            Logout
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
};