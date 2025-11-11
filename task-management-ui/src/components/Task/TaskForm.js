import React, { useMemo, useState } from 'react';
import { taskAPI } from '../../services/api';

const initialTaskState = {
  title: '',
  description: '',
  dueDate: '',
};

const TaskForm = ({ isAdmin = false, onTaskCreated }) => {
  const [task, setTask] = useState(initialTaskState);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const helperText = useMemo(() => {
    if (!isAdmin) {
      return 'Only administrators can create official tasks. Submit details and an administrator will review the request.';
    }
    return 'Provide a short, action-oriented title and outline the expected outcome.';
  }, [isAdmin]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setTask((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    setError('');
    setSuccess('');

    try {
      await taskAPI.createTask(task);
      const message = isAdmin ? 'Task created successfully.' : 'Task submitted for review successfully.';
      setSuccess(message);
      setTask(initialTaskState);
      onTaskCreated?.();
    } catch (err) {
      const message = err?.response?.data?.message ?? 'We could not create the task. Please try again later.';
      setError(message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit} noValidate>
      <div className="form-field">
        <label className="form-label" htmlFor="task-title">
          Task title
        </label>
        <input
          id="task-title"
          className="form-input"
          type="text"
          name="title"
          value={task.title}
          onChange={handleChange}
          placeholder="e.g. Publish weekly product update"
          maxLength={120}
          required
        />
      </div>

      <div className="form-field">
        <label className="form-label" htmlFor="task-description">
          Description
        </label>
        <textarea
          id="task-description"
          className="form-textarea"
          name="description"
          value={task.description}
          onChange={handleChange}
          placeholder="Share the context, deliverables, and any helpful resources for this task."
          required
          minLength={10}
        />
        <span className="form-helper">{helperText}</span>
      </div>

      <div className="form-field">
        <label className="form-label" htmlFor="task-due-date">
          Due date
        </label>
        <input
          id="task-due-date"
          className="form-input"
          type="datetime-local"
          name="dueDate"
          value={task.dueDate}
          onChange={handleChange}
        />
      </div>

      {error && (
        <div className="alert alert--error" role="alert">
          {error}
        </div>
      )}
      {success && (
        <div className="alert alert--success" role="status">
          {success}
        </div>
      )}

      <div>
        <button className="button button--primary" type="submit" disabled={submitting}>
          {submitting ? 'Creating task…' : isAdmin ? 'Create task' : 'Submit for review'}
        </button>
      </div>
    </form>
  );
};

export default TaskForm;
