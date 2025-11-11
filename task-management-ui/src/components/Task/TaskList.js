import React, { useCallback, useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { taskAPI } from '../../services/api';
import Loader from '../common/Loader';
import EmptyState from '../common/EmptyState';
import { formatDateTime, safeArray } from '../../utils/formatters';

const TaskList = ({ refreshKey = 0 }) => {
  const { user } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [updating, setUpdating] = useState(null);

  const loadTasks = useCallback(async () => {
    try {
      setLoading(true);
      setError('');
      const response = user?.role === 'admin' ? await taskAPI.getAllTasks() : await taskAPI.getUserTasks();
      setTasks(safeArray(response?.data));
    } catch (err) {
      const message = err?.response?.data?.message ?? 'We were unable to load your tasks.';
      setError(message);
      setTasks([]);
    } finally {
      setLoading(false);
    }
  }, [user?.role]);

  useEffect(() => {
    if (user) {
      loadTasks();
    }
  }, [loadTasks, user, refreshKey]);

  const handleCompleteTask = async (taskId) => {
    setUpdating(taskId);
    setError('');
    try {
      await taskAPI.completeTask(taskId);
      await loadTasks();
    } catch (err) {
      const message = err?.response?.data?.message ?? 'We could not update the task.';
      setError(message);
    } finally {
      setUpdating(null);
    }
  };

  if (loading) {
    return <Loader label="Loading tasks" />;
  }

  if (error && !tasks.length) {
    return <EmptyState title="We could not load tasks" description={error} />;
  }

  if (!tasks.length) {
    return <EmptyState title="No tasks yet" description="Tasks assigned to you will appear here." />;
  }

  return (
    <div className="task-grid" role="list" aria-label="Task list">
      {error && (
        <div className="alert alert--error" role="alert">
          {error}
        </div>
      )}
      {tasks.map((task) => {
        const taskId = task?.id ?? task?.taskId;
        const isComplete = Boolean(task?.completed);
        return (
          <article key={taskId} className="card task-card" role="listitem">
            <header>
              <h3 className="task-card__title">{task?.title}</h3>
              <p style={{ margin: '0 0 1rem', color: 'var(--color-muted)' }}>{task?.description}</p>
              {task?.dueDate && (
                <div className="task-card__meta">
                  <span>Due {formatDateTime(task?.dueDate)}</span>
                </div>
              )}
            </header>
            <footer className="task-card__meta">
              <span className={`status-badge ${isComplete ? 'status-badge--success' : 'status-badge--warning'}`}>
                {isComplete ? 'Completed' : 'In progress'}
              </span>
              {!isComplete && (
                <button
                  type="button"
                  className="button button--primary"
                  onClick={() => handleCompleteTask(taskId)}
                  disabled={updating === taskId}
                >
                  {updating === taskId ? 'Updating…' : 'Mark complete'}
                </button>
              )}
            </footer>
          </article>
        );
      })}
    </div>
  );
};

export default TaskList;
