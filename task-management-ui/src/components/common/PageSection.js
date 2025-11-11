import React from 'react';

const PageSection = ({ title, description, action, children }) => (
  <section className="card" aria-labelledby={title ? `${title.replace(/\s+/g, '-').toLowerCase()}-heading` : undefined}>
    {(title || description || action) && (
      <header className="section-heading-group" style={{ marginBottom: '1.5rem', display: 'grid', gap: '0.75rem' }}>
        <div>
          {title && (
            <h2
              id={`${title.replace(/\s+/g, '-').toLowerCase()}-heading`}
              className="section-heading"
              style={{ marginBottom: description ? '0.5rem' : 0 }}
            >
              {title}
            </h2>
          )}
          {description && <p className="section-subheading">{description}</p>}
        </div>
        {action}
      </header>
    )}
    {children}
  </section>
);

export default PageSection;
