import React, { Suspense, lazy } from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import AppLayout from './components/Layout/AppLayout';
import Loader from './components/common/Loader';

const Login = lazy(() => import('./components/Auth/Login'));
const Register = lazy(() => import('./components/Auth/Register'));
const Dashboard = lazy(() => import('./components/Layout/Dashboard'));
const SubmissionList = lazy(() => import('./components/Submission/SubmissionList'));

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <Loader label="Preparing your workspace" />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return <AppLayout>{children}</AppLayout>;
};

const PublicRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <Loader label="Preparing your workspace" />;
  }

  if (isAuthenticated) {
    const redirectTo = location.state?.from?.pathname ?? '/dashboard';
    return <Navigate to={redirectTo} replace />;
  }

  return <div className="auth-layout">{children}</div>;
};

function App() {
  return (
    <AuthProvider>
      <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
        <Suspense fallback={<Loader label="Loading interface" />}>
          <Routes>
            <Route
              path="/login"
              element={(
                <PublicRoute>
                  <Login />
                </PublicRoute>
              )}
            />
            <Route
              path="/register"
              element={(
                <PublicRoute>
                  <Register />
                </PublicRoute>
              )}
            />
            <Route
              path="/dashboard"
              element={(
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              )}
            />
            <Route
              path="/submissions"
              element={(
                <ProtectedRoute>
                  <SubmissionList />
                </ProtectedRoute>
              )}
            />
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </Suspense>
      </Router>
    </AuthProvider>
  );
}

export default App;
