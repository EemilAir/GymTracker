package backendsyksy24.gymtracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;

import backendsyksy24.gymtracker.dto.RegisterRequest;
import backendsyksy24.gymtracker.model.AppUser;
import backendsyksy24.gymtracker.repository.AppUserRepository;
import backendsyksy24.gymtracker.util.JwtUtil;
import backendsyksy24.gymtracker.web.UserDetailServiceImpl;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Register user successfully")
    public void testRegisterUserSuccess() throws Exception {
        // Reset mock interactions to ensure a clean state
        Mockito.reset(appUserRepository);

        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("newuser", "StrongP@ssw0rd!");
        AppUser savedUser = new AppUser();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setPasswordHash("encodedPassword");
        savedUser.setRole("ROLE_USER"); // Ensure role prefix

        when(appUserRepository.existsByUsername("newuser")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("StrongP@ssw0rd!")).thenReturn("encodedPassword");
        when(appUserRepository.save(ArgumentMatchers.any(AppUser.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/register") // Ensure the endpoint matches your controller mapping
                .with(csrf()) // Add CSRF token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Verify that the repository methods are called the expected number of times
        verify(appUserRepository, times(1)).existsByUsername("newuser");
        verify(bCryptPasswordEncoder, times(1)).encode("StrongP@ssw0rd!");
        verify(appUserRepository, times(1)).save(ArgumentMatchers.any(AppUser.class));

        // Optionally, ensure no other interactions occurred
        verifyNoMoreInteractions(appUserRepository);
    }

    @Test
    @DisplayName("Register user with existing username")
    public void testRegisterUserDuplicateUsername() throws Exception {
        // Reset mock interactions to ensure a clean state
        Mockito.reset(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("existinguser", "AnotherStrongP@ssw0rd!");
    
        when(appUserRepository.existsByUsername("existinguser")).thenReturn(true);
    
        // Act & Assert
        mockMvc.perform(post("/api/register")
                .with(csrf()) // Add CSRF token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already taken"));
    
        // Verify that the repository methods are called the expected number of times
        verify(appUserRepository, times(1)).existsByUsername("existinguser");
        verify(bCryptPasswordEncoder, times(0)).encode(anyString());
        verify(appUserRepository, times(0)).save(any(AppUser.class));
    
        // Optionally, ensure no other interactions occurred
        verifyNoMoreInteractions(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    }

    @Test
    @DisplayName("Register user with weak password")
    public void testRegisterUserWeakPassword() throws Exception {
        // Reset mock interactions to ensure a clean state
        Mockito.reset(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("weakpassworduser", "weakpass");
    
        // Act & Assert
        mockMvc.perform(post("/api/register")
                .with(csrf()) // Add CSRF token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password is not strong enough"));
    
        // Verify that the repository methods are called the expected number of times
        verify(appUserRepository, times(1)).existsByUsername("weakpassworduser");
        verify(bCryptPasswordEncoder, times(0)).encode(anyString());
        verify(appUserRepository, times(0)).save(any(AppUser.class));
    
        // Optionally, ensure no other interactions occurred
        verifyNoMoreInteractions(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    }

    @Test
    @DisplayName("Login user with correct credentials")
    public void testLoginUserSuccess() throws Exception {
        // Reset mock interactions to ensure a clean state
        Mockito.reset(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    
        // Arrange
        String username = "admin";
        String rawPassword = "admin123";
        String encodedPassword = "$2a$10$D9Qbn4qgPW2hG2pGyvL5MeF8KqVsBxF8Xg8/8P8Jk0VDnXQ1CDYB2";
    
        AppUser existingUser = new AppUser();
        existingUser.setId(1L);
        existingUser.setUsername(username);
        existingUser.setPasswordHash(encodedPassword);
        existingUser.setRole("ADMIN");
    
        String generatedToken = "valid.jwt.token";
    
        // Mocking behaviors
        when(appUserRepository.findByUsername(username.toLowerCase())).thenReturn(existingUser);
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(username)).thenReturn(generatedToken);
    
        // Create login request
        RegisterRequest loginRequest = new RegisterRequest();
        loginRequest.setUsername(username);
        loginRequest.setPasswordHash(rawPassword);
    
        // Act & Assert
        mockMvc.perform(post("/api/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.token").value(generatedToken));
    
        // Verify interactions
        verify(appUserRepository, times(1)).findByUsername(username.toLowerCase());
        verify(bCryptPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(username);
        verifyNoMoreInteractions(appUserRepository, bCryptPasswordEncoder, jwtUtil, userDetailService);
    }
}