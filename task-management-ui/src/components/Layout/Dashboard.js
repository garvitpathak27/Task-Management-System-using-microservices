import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import PageSection from '../common/PageSection';
import TaskForm from '../Task/TaskForm';
import TaskList from '../Task/TaskList';
import AdminPanel from '../Admin/AdminPanel';

const Dashboard = () => {
  const { user } = useAuth();
  const isAdmin = user?.role === 'admin';
  const [taskRefreshKey, setTaskRefreshKey] = useState(0);

  return (
    <div className="grid" style={{ gap: '2.5rem' }}>
      <header className="card" style={{ display: 'grid', gap: '0.75rem' }}>
        <div style={{ display: 'grid', gap: '0.5rem' }}>
          <p className="status-badge status-badge--muted" style={{ justifySelf: 'start' }}>
            {isAdmin ? 'Administrator' : 'Team Member'}
          </p>
          <h1 className="section-heading" style={{ margin: 0 }}>
            Welcome back, {user?.fullName || user?.email}
          </h1>
          <p className="section-subheading" style={{ margin: 0 }}>
            Stay on top of your assignments, collaborate with your team, and keep tasks moving forward.
          </p>
        </div>
      </header>

      {isAdmin && (
        <PageSection
          title="Admin overview"
          description="Monitor user activity, review submissions, and assign work across the organisation."
        >
          <AdminPanel />
        </PageSection>
      )}

      <PageSection
        title={isAdmin ? 'Create a new task' : 'Submit a task idea'}
        description={
          isAdmin
            ? 'Define clear requirements and due dates to keep your team aligned. '
            : 'Share task details so your manager can review and prioritise the work.'
        }
      >
        <TaskForm isAdmin={isAdmin} onTaskCreated={() => setTaskRefreshKey((value) => value + 1)} />
      </PageSection>

      <PageSection
        title="Your tasks"
        description="Track your progress, mark tasks complete, and view the history of every item assigned to you."
      >
        <TaskList refreshKey={taskRefreshKey} />
      </PageSection>
    </div>
  );
};

export default Dashboard;
