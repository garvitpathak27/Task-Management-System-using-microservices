import React, { useEffect, useState } from 'react';
import { submissionAPI, taskAPI, userAPI } from '../../services/api';
import Loader from '../common/Loader';
import EmptyState from '../common/EmptyState';
import { formatDateTime, safeArray } from '../../utils/formatters';

const AdminPanel = () => {
  const [users, setUsers] = useState([]);
  const [submissions, setSubmissions] = useState([]);
  const [newTask, setNewTask] = useState({ title: '', description: '', dueDate: '' });
  const [loading, setLoading] = useState(true);
  const [dashboardError, setDashboardError] = useState('');
  const [taskError, setTaskError] = useState('');
  const [creatingTask, setCreatingTask] = useState(false);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        setLoading(true);
        const [usersResponse, submissionsResponse] = await Promise.all([
          userAPI.getAllUsers(),
          submissionAPI.getAllSubmissions(),
        ]);
        setUsers(safeArray(usersResponse?.data));
        setSubmissions(safeArray(submissionsResponse?.data));
      } catch (err) {
        const message = err?.response?.data?.message ?? 'We were unable to load admin data.';
        setDashboardError(message);
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  const handleTaskChange = (event) => {
    const { name, value } = event.target;
    setNewTask((prev) => ({ ...prev, [name]: value }));
  };

  const handleCreateTask = async (event) => {
    event.preventDefault();
    setCreatingTask(true);
    setTaskError('');
    try {
      await taskAPI.createTask(newTask);
      setNewTask({ title: '', description: '', dueDate: '' });
    } catch (err) {
      const message = err?.response?.data?.message ?? 'The task could not be created.';
      setTaskError(message);
    } finally {
      setCreatingTask(false);
    }
  };

  if (loading) {
    return <Loader label="Loading admin overview" />;
  }

  return (
    <div className="grid" style={{ gap: '2rem' }}>
      {dashboardError && (
        <div className="alert alert--error" role="alert">
          {dashboardError}
        </div>
      )}
      <form className="card form" onSubmit={handleCreateTask} noValidate>
        <header>
          <h3 className="section-heading" style={{ marginBottom: '0.25rem' }}>
            Quick task creation
          </h3>
          <p className="section-subheading" style={{ margin: 0 }}>
            Create work items directly from the admin panel.
          </p>
        </header>
        <div className="form-field">
          <label className="form-label" htmlFor="admin-task-title">
            Title
          </label>
          <input
            id="admin-task-title"
            className="form-input"
            name="title"
            value={newTask.title}
            onChange={handleTaskChange}
            required
          />
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="admin-task-description">
            Description
          </label>
          <textarea
            id="admin-task-description"
            className="form-textarea"
            name="description"
            value={newTask.description}
            onChange={handleTaskChange}
            required
            minLength={10}
          />
        </div>
        <div className="form-field">
          <label className="form-label" htmlFor="admin-task-due-date">
            Due date
          </label>
          <input
            id="admin-task-due-date"
            className="form-input"
            type="datetime-local"
            name="dueDate"
            value={newTask.dueDate}
            onChange={handleTaskChange}
          />
        </div>
        {taskError && (
          <div className="alert alert--error" role="alert">
            {taskError}
          </div>
        )}
        <div>
          <button className="button button--primary" type="submit" disabled={creatingTask}>
            {creatingTask ? 'Creating…' : 'Create task'}
          </button>
        </div>
      </form>

      <section className="card" aria-labelledby="admin-users-heading">
        <header style={{ marginBottom: '1.25rem' }}>
          <h3 id="admin-users-heading" className="section-heading" style={{ margin: 0 }}>
            Team members
          </h3>
        </header>
        {users.length ? (
          <div className="table-list" role="list">
            {users.map((user) => (
              <article key={user?.id ?? user?.email} role="listitem" className="card" style={{ boxShadow: 'none' }}>
                <div style={{ fontWeight: 600 }}>{user?.fullName ?? user?.email}</div>
                <div style={{ color: 'var(--color-muted)', fontSize: '0.9rem' }}>{user?.email}</div>
                <span className="status-badge status-badge--muted" style={{ marginTop: '0.5rem' }}>
                  {user?.role ?? 'User'}
                </span>
              </article>
            ))}
          </div>
        ) : (
          <EmptyState title="No users" description="Invite colleagues to collaborate on tasks." />
        )}
      </section>

      <section className="card" aria-labelledby="admin-submissions-heading">
        <header style={{ marginBottom: '1.25rem' }}>
          <h3 id="admin-submissions-heading" className="section-heading" style={{ margin: 0 }}>
            Recent submissions
          </h3>
        </header>
        {submissions.length ? (
          <div className="table-list" role="list">
            {submissions.slice(0, 8).map((submission) => (
              <article key={submission?.id} role="listitem" className="card" style={{ boxShadow: 'none' }}>
                <div style={{ display: 'grid', gap: '0.35rem' }}>
                  <div style={{ fontWeight: 600 }}>Task #{submission?.taskId}</div>
                  <p style={{ margin: 0, color: 'var(--color-muted)' }}>{submission?.content}</p>
                </div>
                <div className="submission-card__meta" style={{ marginTop: '0.85rem' }}>
                  <span
                    className={`status-badge ${
                      submission?.status === 'submitted' ? 'status-badge--success' : 'status-badge--warning'
                    }`}
                  >
                    {submission?.status ?? 'Pending'}
                  </span>
                  {submission?.createdAt && <span>{formatDateTime(submission?.createdAt)}</span>}
                </div>
              </article>
            ))}
          </div>
        ) : (
          <EmptyState title="No submissions" description="Submissions will appear here once team members upload work." />
        )}
      </section>
    </div>
  );
};

export default AdminPanel;
