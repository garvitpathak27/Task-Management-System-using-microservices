import React, { useState, useEffect } from 'react';
import { taskAPI, userAPI, submissionAPI } from '../../services/api';

const AdminPanel = () => {
  const [users, setUsers] = useState([]);
  const [submissions, setSubmissions] = useState([]);
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    dueDate: ''
  });

  useEffect(() => {
    loadUsers();
    loadSubmissions();
  }, []);

  const loadUsers = async () => {
  try {
    const response = await userAPI.getAllUsers();
    // Add safety check here too
    const usersArray = Array.isArray(response.data) ? response.data : [];
    setUsers(usersArray);
  } catch (error) {
    console.error('Error loading users:', error);
    setUsers([]);
  }
};


  const loadSubmissions = async () => {
  try {
    const response = await submissionAPI.getAllSubmissions();
    console.log('📝 AdminPanel submissions response:', response.data);
    
    // Add this safety check - ensure we always have an array
    const submissionsArray = Array.isArray(response.data) ? response.data : [];
    setSubmissions(submissionsArray);
  } catch (error) {
    console.error('Error loading submissions:', error);
    setSubmissions([]); // Always fallback to empty array
  }
};

  const handleCreateTask = async (e) => {
    e.preventDefault();
    try {
      await taskAPI.createTask(newTask);
      setNewTask({ title: '', description: '', dueDate: '' });
      alert('Task created successfully!');
    } catch (error) {
      alert('Error creating task');
    }
  };

  const handleAssignTask = async (taskId, userId) => {
    try {
      await taskAPI.assignTask(taskId, userId);
      alert('Task assigned successfully!');
    } catch (error) {
      alert('Error assigning task');
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '8px' }}>
      <h2>Admin Panel</h2>
      
      {/* Create Task Form */}
      <div style={{ marginBottom: '30px' }}>
        <h3>Create New Task</h3>
        <form onSubmit={handleCreateTask}>
          <input
            type="text"
            placeholder="Task Title"
            value={newTask.title}
            onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
            required
            style={{ width: '100%', padding: '10px', marginBottom: '10px' }}
          />
          <textarea
            placeholder="Task Description"
            value={newTask.description}
            onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
            required
            style={{ width: '100%', padding: '10px', marginBottom: '10px', height: '80px' }}
          />
          <input
            type="datetime-local"
            value={newTask.dueDate}
            onChange={(e) => setNewTask({ ...newTask, dueDate: e.target.value })}
            style={{ width: '100%', padding: '10px', marginBottom: '10px' }}
          />
          <button
            type="submit"
            style={{
              padding: '10px 20px',
              backgroundColor: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '4px'
            }}
          >
            Create Task
          </button>
        </form>
      </div>

      {/* Users List */}
      <div style={{ marginBottom: '30px' }}>
        <h3>Users</h3>
        {users.length > 0 ? (
          <div style={{ display: 'grid', gap: '10px' }}>
            {users.map((user) => (
              <div
                key={user.id}
                style={{
                  border: '1px solid #ddd',
                  padding: '10px',
                  borderRadius: '4px',
                  backgroundColor: 'white'
                }}
              >
                <strong>{user.fullName}</strong> - {user.email}
                <span style={{ color: '#666', marginLeft: '10px' }}>
                  ({user.role || 'User'})
                </span>
              </div>
            ))}
          </div>
        ) : (
          <p>No users found.</p>
        )}
      </div>

      {/* Submissions */}
      <div>
        <h3>Recent Submissions</h3>
        {submissions.length > 0 ? (
          <div style={{ display: 'grid', gap: '10px' }}>
            {submissions.slice(0, 5).map((submission) => (
              <div
                key={submission.id}
                style={{
                  border: '1px solid #ddd',
                  padding: '10px',
                  borderRadius: '4px',
                  backgroundColor: 'white'
                }}
              >
                <strong>Task ID:</strong> {submission.taskId}<br />
                <strong>Content:</strong> {submission.content}<br />
                <strong>Status:</strong> {submission.status}
              </div>
            ))}
          </div>
        ) : (
          <p>No submissions found.</p>
        )}
      </div>
    </div>
  );
};

export default AdminPanel;
