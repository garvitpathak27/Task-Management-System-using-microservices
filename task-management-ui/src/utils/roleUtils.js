/**
 * Utility functions for role-based access control
 */

/**
 * Check if a user has admin privileges
 * @param {Object} user - User object with role property
 * @returns {boolean} True if user is an admin
 */
export const isAdmin = (user) => {
  if (!user || !user.role) return false;
  const role = user.role.toUpperCase();
  return role === 'ROLE_ADMIN' || role === 'ADMIN';
};

/**
 * Check if a user has a specific role
 * @param {Object} user - User object with role property
 * @param {string} roleName - Role name to check (with or without ROLE_ prefix)
 * @returns {boolean} True if user has the role
 */
export const hasRole = (user, roleName) => {
  if (!user || !user.role || !roleName) return false;
  
  const userRole = user.role.toUpperCase();
  const checkRole = roleName.toUpperCase();
  
  // Handle both formats: "ADMIN" and "ROLE_ADMIN"
  const roleWithPrefix = checkRole.startsWith('ROLE_') ? checkRole : `ROLE_${checkRole}`;
  const roleWithoutPrefix = checkRole.replace('ROLE_', '');
  
  return userRole === roleWithPrefix || userRole === roleWithoutPrefix;
};

/**
 * Get display name for a role
 * @param {string} role - Role string (e.g., "ROLE_ADMIN", "ROLE_CUSTOMER")
 * @returns {string} Human-readable role name
 */
export const getRoleDisplayName = (role) => {
  if (!role) return 'User';
  
  const roleMap = {
    'ROLE_ADMIN': 'Administrator',
    'ADMIN': 'Administrator',
    'ROLE_CUSTOMER': 'Team Member',
    'CUSTOMER': 'Team Member',
    'ROLE_USER': 'User',
    'USER': 'User',
  };
  
  return roleMap[role.toUpperCase()] || 'User';
};

export default {
  isAdmin,
  hasRole,
  getRoleDisplayName,
};
