import React from 'react';
import styled from 'styled-components';

const StyledFooter = styled.footer`
  background-color: #333333;
  color: #ffffff;
  padding: 5px;
  text-align: center;
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;

  /* Responsive */
  @media (max-width: 768px) {
    flex-direction: column;
    gap: 15px;
  }
`;

const FooterSection = styled.div`
  flex: 1;
  min-width: 200px;
`;

const GitHubLink = styled.a`
  color: #ffffff;
  text-decoration: underline;
  font-weight: bold;

  &:hover {
    color: #dddddd;
  }
`;

const Footer = () => {
  return (
    <StyledFooter>
      <FooterSection>
      <p>&copy; {new Date().getFullYear()} GymTracker. All rights reserved.</p>
      <p>Powered by Me and Myself, {' '}
        <GitHubLink
          href="https://github.com/EemilAir"
          target="_blank"
          rel="noopener noreferrer">
          GitHub
        </GitHubLink>
      </p>
      </FooterSection>
    </StyledFooter>
  );
};

export default Footer;