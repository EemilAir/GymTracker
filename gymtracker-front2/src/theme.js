// src/theme.js
import { createGlobalStyle } from 'styled-components';

// Define your theme object
const theme = {
  colors: {
    primary: '#007bff',
    secondary: '#6c757d',
    success: '#28a745',
    danger: '#dc3545',
    warning: '#ffc107',
    info: '#17a2b8',
    light: '#f8f9fa',
    dark: '#343a40',
    background: '#f5f5f5',
    text: '#212529',
    white: '#ffffff',
  },
  fonts: {
    main: `'Roboto', sans-serif`,
    code: `'Courier New', Courier, monospace`,
  },
  fontSizes: {
    small: '0.875rem',
    medium: '1rem',
    large: '1.25rem',
    xlarge: '1.5rem',
  },
  spacing: {
    xs: '0.25rem',
    sm: '0.5rem',
    md: '1rem',
    lg: '1.5rem',
    xl: '3rem',
  },
  breakpoints: {
    mobile: '768px',
    tablet: '1024px',
  },
  shadows: {
    small: '0 1px 3px rgba(0,0,0,0.12)',
    medium: '0 4px 6px rgba(0,0,0,0.1)',
    large: '0 10px 20px rgba(0,0,0,0.19)',
  },
  borderRadius: {
    small: '4px',
    medium: '8px',
    large: '12px',
    circular: '50%',
  },
};

// Create global styles to apply base styles using the theme
export const GlobalStyle = createGlobalStyle`
  * {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    padding: 0;
    font-family: ${(props) => props.theme.fonts.main};
    background-color: ${(props) => props.theme.colors.background};
    color: ${(props) => props.theme.colors.text};
  }

  h1, h2, h3, h4, h5, h6 {
    color: ${(props) => props.theme.colors.dark};
    margin: 0 0 ${(props) => props.theme.spacing.sm} 0;
    font-family: ${(props) => props.theme.fonts.main};
  }

  p {
    margin: 0 0 ${(props) => props.theme.spacing.sm} 0;
    line-height: 1.6;
  }

  button {
    font-family: ${(props) => props.theme.fonts.main};
  }

  /* Add more global styles as needed */
`;

export default theme;