import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const { pathname } = useLocation();

  if (!isAuthenticated) {
    return null;
  }

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <header className="navbar" role="banner">
      <div className="navbar__inner" role="navigation" aria-label="Primary">
        <h1 className="navbar__brand">Task Management System</h1>
        <div className="navbar__actions">
          <span className="navbar__user">{user?.fullName || user?.email}</span>
          <Link
            to="/dashboard"
            className={`button button--ghost${pathname === '/dashboard' ? ' is-active' : ''}`}
            aria-current={pathname === '/dashboard' ? 'page' : undefined}
          >
            Dashboard
          </Link>
          <Link
            to="/submissions"
            className={`button button--ghost${pathname === '/submissions' ? ' is-active' : ''}`}
            aria-current={pathname === '/submissions' ? 'page' : undefined}
          >
            Submissions
          </Link>
          <button type="button" className="button button--danger" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
