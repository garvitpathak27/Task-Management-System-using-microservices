import React, { useState, useEffect } from 'react';
import { taskAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadTasks();
  }, [user]);

  const loadTasks = async () => {
    try {
      let response;
      if (user?.role === 'admin') {
        response = await taskAPI.getAllTasks();
      } else {
        response = await taskAPI.getUserTasks();
      }
      setTasks(response.data);
    } catch (error) {
      console.error('Error loading tasks:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteTask = async (taskId) => {
    try {
      await taskAPI.completeTask(taskId);
      loadTasks(); // Refresh tasks
      alert('Task marked as complete!');
    } catch (error) {
      alert('Error completing task');
    }
  };

  if (loading) return <div>Loading tasks...</div>;

  return (
    <div>
      <h2>My Tasks</h2>
      {tasks.length === 0 ? (
        <p>No tasks available.</p>
      ) : (
        <div style={{ display: 'grid', gap: '15px' }}>
          {tasks.map((task) => (
            <div
              key={task.id}
              style={{
                border: '1px solid #ddd',
                borderRadius: '8px',
                padding: '15px',
                backgroundColor: task.completed ? '#f8f9fa' : 'white'
              }}
            >
              <h3>{task.title}</h3>
              <p>{task.description}</p>
              <div style={{ marginTop: '10px' }}>
                <span style={{
                  padding: '4px 8px',
                  borderRadius: '4px',
                  backgroundColor: task.completed ? '#28a745' : '#ffc107',
                  color: task.completed ? 'white' : 'black',
                  fontSize: '12px'
                }}>
                  {task.completed ? 'Completed' : 'In Progress'}
                </span>
              </div>
              {!task.completed && (
                <button
                  onClick={() => handleCompleteTask(task.id)}
                  style={{
                    marginTop: '10px',
                    padding: '8px 16px',
                    backgroundColor: '#007bff',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer'
                  }}
                >
                  Mark Complete
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TaskList;
