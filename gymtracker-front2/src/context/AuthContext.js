import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

// The AuthProvider component is used to manage the authentication state of the user.
export const AuthProvider = ({ children }) => {
  const [authToken, setAuthToken] = useState(() => localStorage.getItem('authToken')); 
  const [userId, setUserId] = useState(() => localStorage.getItem('userId'));
  const [username, setUsername] = useState(() => localStorage.getItem('username'));

  // The login function is used to set the authToken and userId in the state and localStorage.
  const login = (token, id, user) => {
    localStorage.setItem('authToken', token); 
    localStorage.setItem('userId', id);
    localStorage.setItem('username', user);
    setAuthToken(token); 
    setUserId(id);
    setUsername(user);
  };

  // The logout function is used to remove the authToken and userId from the state and localStorage.
  const logout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    setAuthToken(null);
    setUserId(null);
    setUsername(null);
  };

  // Logs the authToken, userId, and username whenever they change.
  useEffect(() => {
    console.log('AuthProvider updated. authToken:', authToken, 'userId:', userId, 'username:', username);
  }, [authToken, userId, username]);

  return (
    <AuthContext.Provider value={{ authToken, userId, username, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};