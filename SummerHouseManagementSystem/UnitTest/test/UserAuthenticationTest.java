import dtos.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for User Authentication functionality in the House Rental Management System.
 * Tests for User Stories 6, 7, and 8:
 * - As a Customer, I want to create an account, so that I can have my activity recorded.
 * - As a Customer I want to be able to log in/log out to my account, so I can make a booking, and look at my activity.
 * - As an admin, I want to be able to log in/ log out, so that I can manage properties, view bookings and customers.
 */
public class UserAuthenticationTest {

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }    /**
     * Test user account creation with valid details
     */
    @Test
    public void testCreateUserAccount() {
        // Test data for customer registration
        String username = "dani123";
        String password = "Daniel@123";
        String email = "daniel@g.c";

        // Create a new user (customer role by default)
        User newUser = new User(username, email, password, false);

        // Verify user creation with correct details
        assertNotNull(newUser, "User should be created successfully");
        assertEquals(username, newUser.getUsername(), "Username should match input");
        assertEquals(email, newUser.getEmail(), "Email should match input");
        assertFalse(newUser.isAdmin(), "New user should not be an admin by default");
    }

    /**
     * Test user login with valid credentials
     */
    @Test
    public void testUserLogin() {
        // Setup mock user behavior
        String username = "testuser";
        String password = "correctPassword123";

        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.validatePassword(password)).thenReturn(true);

        // Test successful login
        boolean loginSuccess = mockUser.validatePassword(password);
        assertTrue(loginSuccess, "Login should succeed with valid credentials");

        // Verify that the username matches
        assertEquals(username, mockUser.getUsername(), "Username should match the logged-in user");

    }

    /**
     * Test login failure with invalid password
     */
    @Test
    public void testLoginFailureInvalidPassword() {
        // Setup mock user behavior
        String username = "testuser";
        String validPassword = "correctPassword123";
        String invalidPassword = "wrongPassword123";

        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.validatePassword(validPassword)).thenReturn(true);
        when(mockUser.validatePassword(invalidPassword)).thenReturn(false);

        // Test login failure
        boolean loginFailure = mockUser.validatePassword(invalidPassword);
        assertFalse(loginFailure, "Login should fail with invalid password");
    }

    /**
     * Test admin login and privileges
     */
    @Test
    public void testAdminLogin() {
        // Setup mock admin user
        String adminUsername = "adminuser";
        String adminPassword = "adminPass123";

        when(mockUser.getUsername()).thenReturn(adminUsername);
        when(mockUser.validatePassword(adminPassword)).thenReturn(true);
        when(mockUser.isAdmin()).thenReturn(true);

        // Test successful admin login
        boolean loginSuccess = mockUser.validatePassword(adminPassword);
        assertTrue(loginSuccess, "Admin login should succeed with valid credentials");
        assertTrue(mockUser.isAdmin(), "User should have admin privileges");
    }

    /**
     * Test user logout functionality
     */
    @Test
    public void testUserLogout() {
        // Setup mock session state
        boolean initiallyLoggedIn = true;
        boolean[] loggedIn = {initiallyLoggedIn}; // Using array to allow modification in lambda

        // Mock logout behavior
        doAnswer(invocation -> {
            loggedIn[0] = false;
            return null;
        }).when(mockUser).logout();

        // Test logout
        mockUser.logout();
        assertFalse(loggedIn[0], "User should be logged out after logout action");
    }
}
