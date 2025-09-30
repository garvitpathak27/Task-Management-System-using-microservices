import React, { useState } from 'react';
import { taskAPI } from '../../services/api';

const TaskForm = () => {
  const [task, setTask] = useState({
    title: '',
    description: ''
  });

 const handleSubmit = async (e) => {
  e.preventDefault();
  
  try {
    console.log('🔍 Creating task:', task);
    console.log('🔑 Auth token:', localStorage.getItem('authToken'));
    
    const response = await taskAPI.createTask(task);
    alert('✅ Task created successfully!');
    setTask({ title: '', description: '' });
  } catch (error) {
    console.error('❌ Full error:', error);
    console.error('📊 Error response:', error.response?.data);
    console.error('🔢 Status code:', error.response?.status);
    
    alert('Failed to create task. Check console and backend logs.');
  }
};


  return (
    <div style={{ backgroundColor: '#f0f8ff', padding: '20px', margin: '20px 0' }}>
      <h3>Create New Task</h3>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Task Title"
          value={task.title}
          onChange={(e) => setTask({...task, title: e.target.value})}
          style={{ width: '100%', padding: '10px', margin: '10px 0' }}
        />
        <textarea
          placeholder="Task Description"
          value={task.description}
          onChange={(e) => setTask({...task, description: e.target.value})}
          style={{ width: '100%', padding: '10px', margin: '10px 0', height: '80px' }}
        />
        <button type="submit" style={{ padding: '10px 20px', background: 'green', color: 'white' }}>
          Create Task
        </button>
      </form>
    </div>
  );
};

export default TaskForm;
