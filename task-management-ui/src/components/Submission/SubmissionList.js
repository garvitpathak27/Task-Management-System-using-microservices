import React, { useEffect, useState } from 'react';
import { submissionAPI } from '../../services/api';
import Loader from '../common/Loader';
import EmptyState from '../common/EmptyState';
import { formatDateTime, safeArray } from '../../utils/formatters';

const SubmissionList = () => {
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadSubmissions = async () => {
      try {
        setLoading(true);
        const response = await submissionAPI.getAllSubmissions();
        setSubmissions(safeArray(response?.data));
      } catch (err) {
        const message = err?.response?.data?.message ?? 'We were unable to load submissions.';
        setError(message);
        setSubmissions([]);
      } finally {
        setLoading(false);
      }
    };

    loadSubmissions();
  }, []);

  if (loading) {
    return <Loader label="Loading submissions" />;
  }

  if (error) {
    return <EmptyState title="Unable to show submissions" description={error} />;
  }

  if (!submissions.length) {
    return <EmptyState title="No submissions yet" description="Once team members upload work, it will appear here." />;
  }

  return (
    <div className="grid" style={{ gap: '1.75rem' }}>
      <header className="card" style={{ display: 'grid', gap: '0.75rem' }}>
        <h1 className="section-heading" style={{ margin: 0 }}>
          Submissions overview
        </h1>
        <p className="section-subheading" style={{ margin: 0 }}>
          Review the work your team has shared and keep projects moving forward.
        </p>
      </header>
      <div className="task-grid" role="list" aria-label="Submission list">
        {submissions.map((submission) => (
          <article key={submission?.id} className="card" role="listitem">
            <div style={{ display: 'grid', gap: '0.75rem' }}>
              <header style={{ display: 'grid', gap: '0.35rem' }}>
                <span className="status-badge status-badge--muted">Task #{submission?.taskId}</span>
                <h2 style={{ margin: 0, fontSize: '1.1rem' }}>Submission</h2>
              </header>
              <p style={{ margin: 0, color: 'var(--color-muted)' }}>{submission?.content}</p>
            </div>
            <footer className="submission-card__meta">
              <span
                className={`status-badge ${
                  submission?.status === 'submitted' ? 'status-badge--success' : 'status-badge--warning'
                }`}
              >
                {submission?.status ?? 'Pending'}
              </span>
              {submission?.createdAt && <span>{formatDateTime(submission?.createdAt)}</span>}
            </footer>
          </article>
        ))}
      </div>
    </div>
  );
};

export default SubmissionList;
