import React from 'react';
import Navbar from './Navbar';

const AppLayout = ({ children }) => (
  <div className="app-shell">
    <Navbar />
    <main className="app-content">
      <div className="page-container">{children}</div>
    </main>
  </div>
);

export default AppLayout;
