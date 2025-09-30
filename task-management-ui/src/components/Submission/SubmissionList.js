import React, { useState, useEffect } from 'react';
import { submissionAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const SubmissionList = () => {
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadSubmissions();
  }, []);

const loadSubmissions = async () => {
  try {
    const response = await submissionAPI.getAllSubmissions();
    
    // Safety check: ensure we always have an array
    const submissionsArray = Array.isArray(response.data) ? response.data : [];
    setSubmissions(submissionsArray);
  } catch (error) {
    console.error('Error loading submissions:', error);
    setSubmissions([]); // Always fallback to empty array
  }
};

  if (loading) return <div>Loading submissions...</div>;

  return (
    <div style={{ padding: '20px' }}>
      <h2>Submissions</h2>
      {submissions.length === 0 ? (
        <p>No submissions found.</p>
      ) : (
        <div style={{ display: 'grid', gap: '15px' }}>
          {submissions.map((submission) => (
            <div
              key={submission.id}
              style={{
                border: '1px solid #ddd',
                borderRadius: '8px',
                padding: '15px',
                backgroundColor: 'white'
              }}
            >
              <div style={{ marginBottom: '10px' }}>
                <strong>Task ID:</strong> {submission.taskId}
              </div>
              <div style={{ marginBottom: '10px' }}>
                <strong>Content:</strong>
                <p style={{ marginTop: '5px', padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '4px' }}>
                  {submission.content}
                </p>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{
                  padding: '4px 8px',
                  borderRadius: '4px',
                  backgroundColor: submission.status === 'submitted' ? '#28a745' : '#ffc107',
                  color: 'white',
                  fontSize: '12px'
                }}>
                  {submission.status}
                </span>
                <small style={{ color: '#666' }}>
                  Submitted: {new Date(submission.createdAt).toLocaleDateString()}
                </small>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SubmissionList;
