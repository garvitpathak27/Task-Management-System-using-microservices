import React, { useState } from 'react';
import { submissionAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const SubmissionForm = ({ taskId, onSubmissionCreated }) => {
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const submissionData = {
        taskId,
        userId: user.id,
        content,
        status: 'submitted'
      };
      
      await submissionAPI.createSubmission(submissionData);
      setContent('');
      alert('Submission created successfully!');
      if (onSubmissionCreated) onSubmissionCreated();
    } catch (error) {
      alert('Error creating submission');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginTop: '15px' }}>
      <h4>Submit Your Work</h4>
      <textarea
        placeholder="Enter your submission content..."
        value={content}
        onChange={(e) => setContent(e.target.value)}
        required
        style={{
          width: '100%',
          height: '100px',
          padding: '10px',
          marginBottom: '10px',
          borderRadius: '4px',
          border: '1px solid #ddd'
        }}
      />
      <button
        type="submit"
        disabled={loading}
        style={{
          padding: '8px 16px',
          backgroundColor: '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        {loading ? 'Submitting...' : 'Submit'}
      </button>
    </form>
  );
};

export default SubmissionForm;
