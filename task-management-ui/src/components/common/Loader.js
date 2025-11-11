import React from 'react';

const Loader = ({ label = 'Loading', size = 'md', className = '' }) => {
  return (
    <div className={`loading-state ${className}`.trim()} role="status" aria-live="polite" aria-busy="true">
      <div className={`spinner ${size === 'sm' ? 'spinner--sm' : ''}`.trim()} />
      <span>{label}…</span>
    </div>
  );
};

export default Loader;
