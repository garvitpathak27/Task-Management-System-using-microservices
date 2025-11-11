import React, { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Login = () => {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const redirectPath = location.state?.from?.pathname ?? '/dashboard';

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
      await login(credentials);
      navigate(redirectPath, { replace: true });
    } catch (err) {
      const message = err?.response?.data?.message ?? 'Unable to sign you in. Please try again.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-card" role="main" aria-labelledby="login-heading">
      <header className="auth-card__header">
        <h1 id="login-heading" className="auth-card__title">
          Welcome back
        </h1>
        <p className="auth-card__subtitle">Access your dashboard and manage tasks effortlessly.</p>
      </header>
      <form className="form" onSubmit={handleSubmit} noValidate>
        <div className="form-field">
          <label className="form-label" htmlFor="email">
            Email
          </label>
          <input
            id="email"
            className="form-input"
            type="email"
            name="email"
            autoComplete="email"
            value={credentials.email}
            onChange={(event) => setCredentials((prev) => ({ ...prev, email: event.target.value }))}
            required
          />
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="password">
            Password
          </label>
          <input
            id="password"
            className="form-input"
            type="password"
            name="password"
            autoComplete="current-password"
            value={credentials.password}
            onChange={(event) => setCredentials((prev) => ({ ...prev, password: event.target.value }))}
            required
          />
        </div>
        {error && (
          <div className="alert alert--error" role="alert">
            {error}
          </div>
        )}
        <button className="button button--primary" type="submit" disabled={loading}>
          {loading ? 'Signing in…' : 'Sign in'}
        </button>
      </form>
      <footer className="auth-footer">
        Don't have an account? <Link to="/register">Create one</Link>
      </footer>
    </div>
  );
};

export default Login;
