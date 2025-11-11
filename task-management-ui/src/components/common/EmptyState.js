import React from 'react';

const EmptyState = ({ title = 'Nothing here yet', description = 'Check back soon.' }) => (
  <div className="empty-state" role="status" aria-live="polite">
    <h3>{title}</h3>
    {description && <p>{description}</p>}
  </div>
);

export default EmptyState;
