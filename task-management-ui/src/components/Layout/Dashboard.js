import React from "react";
import { useAuth } from "../../context/AuthContext";
import TaskList from "../Task/TaskList";
import AdminPanel from "../Admin/AdminPanel";
import TaskForm from "../Task/TaskForm";

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div style={{ padding: "20px" }}>
      <h1>Welcome, {user?.fullName || user?.email}!</h1>
      <div style={{ marginBottom: "20px" }}>
        <p>Role: {user?.role || "User"}</p>
      </div>

      {/* Show admin panel for admin users */}
      {user?.role === "admin" && <AdminPanel />}

      {/* Task list for all users */}
      <TaskForm />
      <TaskList />
    </div>
  );
};

export default Dashboard;
