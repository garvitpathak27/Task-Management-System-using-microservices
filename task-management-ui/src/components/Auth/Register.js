import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Register = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    mobile: '',
    role: 'ROLE_CUSTOMER', // Default role
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
  await registerUser(formData);
      navigate('/login', { replace: true, state: { justRegistered: true } });
    } catch (err) {
      const message = err?.response?.data?.message ?? 'We could not complete your registration. Please try again.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-card" role="main" aria-labelledby="register-heading">
      <header className="auth-card__header">
        <h1 id="register-heading" className="auth-card__title">
          Join the workspace
        </h1>
        <p className="auth-card__subtitle">Create an account to collaborate and stay on top of your tasks.</p>
      </header>
      <form className="form" onSubmit={handleSubmit} noValidate>
        <div className="form-field">
          <label className="form-label" htmlFor="fullName">
            Full name
          </label>
          <input
            id="fullName"
            className="form-input"
            type="text"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            autoComplete="name"
            required
          />
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="email">
            Email
          </label>
          <input
            id="email"
            className="form-input"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            autoComplete="email"
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
            value={formData.password}
            onChange={handleChange}
            autoComplete="new-password"
            minLength={6}
            required
          />
          <span className="form-helper">Use at least 6 characters. A mix of letters and numbers works best.</span>
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="mobile">
            Mobile number
          </label>
          <input
            id="mobile"
            className="form-input"
            type="tel"
            name="mobile"
            value={formData.mobile}
            onChange={handleChange}
            autoComplete="tel"
            required
          />
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="role">
            Role
          </label>
          <select
            id="role"
            className="form-input"
            name="role"
            value={formData.role}
            onChange={handleChange}
            required
          >
            <option value="ROLE_CUSTOMER">Team Member</option>
            <option value="ROLE_ADMIN">Administrator</option>
          </select>
          <span className="form-helper">Select Administrator to create and manage tasks for the team.</span>
        </div>
        {error && (
          <div className="alert alert--error" role="alert">
            {error}
          </div>
        )}
        <button className="button button--primary" type="submit" disabled={loading}>
          {loading ? 'Creating account…' : 'Create account'}
        </button>
      </form>
      <footer className="auth-footer">
        Already have an account? <Link to="/login">Sign in</Link>
      </footer>
    </div>
  );
};

export default Register;
