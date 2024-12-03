import React, { useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom'; // Import useLocation
import styled from 'styled-components';
import { AuthContext } from '../context/AuthContext'; // Adjust the path as necessary

const StyledHeader = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  background-color: #003366;
  border-bottom: 1px solid #ddd;
  
  @media (max-width: 768px) {
    flex-direction: column;
    padding: 10px 20px;
  }
`;

const Logo = styled.div`
  font-size: 1.5rem;
  font-weight: bold;
  color: #ffffff;
`;

const Nav = styled.nav`
  display: flex;
  gap: 20px;
`;

const ButtonNavLink = styled.button`
  background: none;
  border: none;
  color: #ffffff;
  font-size: 1rem;
  cursor: pointer;
  padding: 5px 10px;

  &:hover {
    color: #007BFF;
  }

  /* Accessibility */
  &:focus {
    outline: 2px dashed #007BFF;
    outline-offset: 4px;
  }
`;

// The Header component displays the company logo and a Logout button.
const Header = () => {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout(); // Uses AuthContext's logout function
    navigate('/login'); // Redirects to login page
  };

  // Defines paths where Logout button should be hidden
  const hideLogoutPaths = ['/login', '/register'];

  // Determine if the current path is one where Logout should be hidden
  const shouldHideLogout = hideLogoutPaths.includes(location.pathname.toLowerCase());

  return (
    <StyledHeader>
      <Logo aria-label="Company Logo">GymTracker</Logo>
      <Nav>
        {/* Conditionally render Logout button */}
        {!shouldHideLogout && (
          <ButtonNavLink onClick={handleLogout}>
            Logout
          </ButtonNavLink>
        )}
      </Nav>
    </StyledHeader>
  );
};

export default Header;