import React, { useState } from 'react';
import { submissionAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const SubmissionForm = ({ taskId, onSubmissionCreated }) => {
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const { user } = useAuth();
  const canSubmit = Boolean(user?.id && taskId);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const submissionPayload = {
        taskId,
        userId: user?.id,
        content,
        status: 'submitted',
      };

      if (!canSubmit) {
        throw new Error('You must be signed in to submit work.');
      }

      await submissionAPI.createSubmission(submissionPayload);
      setContent('');
      setSuccess('Submission uploaded successfully.');
      onSubmissionCreated?.();
    } catch (err) {
      const message = err?.response?.data?.message ?? 'We could not create your submission.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit} noValidate>
      <h3 style={{ margin: '0 0 0.75rem' }}>Submit your work</h3>
      <div className="form-field">
        <label className="form-label" htmlFor={`submission-content-${taskId}`}>
          Submission details
        </label>
        <textarea
          id={`submission-content-${taskId}`}
          className="form-textarea"
          value={content}
          onChange={(event) => setContent(event.target.value)}
          placeholder="Describe what you accomplished, include links or attachments if needed."
          required
          minLength={10}
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
      <button className="button button--primary" type="submit" disabled={loading || !canSubmit}>
        {loading ? 'Submitting…' : 'Submit work'}
      </button>
    </form>
  );
};

export default SubmissionForm;
