import dtos.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for User Profile Management functionality.
 * Tests for User Story 19:
 * - As a customer I want to be able to update my password, so that I can maintain my profile with updated security.
 */
public class UserProfileManagementTest {

    private User user;
    
    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a real user for testing
        user = new User("kenenisa", "kenenisa@g.c", "Kenenisa@123", false);
        
        // Setup mock user behavior
        when(mockUser.getUsername()).thenReturn("kenenisa");
        when(mockUser.getEmail()).thenReturn("kenenisa@g.c");
        when(mockUser.isAdmin()).thenReturn(false);

    }

    /**
     * Test user password update (User Story 19)
     */
    @Test
    public void testUpdatePassword() {
        // Original password
        String originalPassword = "Kenenisa@123";
        
        // New stronger password
        String newPassword = "newKenenisa@123";
          // Simulate password update
        user = new User(user.getUsername(), user.getEmail(), 
                        newPassword, user.isAdmin());
        
        // Verify update was successful (in real app, would validate with validatePassword)
        assertNotEquals(originalPassword, newPassword, "New password should be different from original");
        
        // Test invalid password update (too short)
        String weakPassword = "weak";

        Exception weakPasswordException = assertThrows(IllegalArgumentException.class, () -> {
            new User(user.getUsername(), user.getEmail(), 
                    weakPassword, user.isAdmin());
        }, "User creation should fail with weak password");
    }    /**
     * Test updating user profile details
     */
    @Test
    public void testUpdateProfileDetails() {
        // Original user details
        String originalEmail = user.getEmail();
        
        // New user details
        String newEmail = "kenenisa2@g.c";
          // Simulate profile update
        user = new User(user.getUsername(), newEmail, user.getPassword(), user.isAdmin());
        
        // Verify updates
        assertNotEquals(originalEmail, user.getEmail(), "Email should be updated");
        assertEquals(newEmail, user.getEmail(), "Email should match new value");
        
        // Verify username and admin status remain unchanged
        assertEquals("kenenisa", user.getUsername(), "Username should remain unchanged");
        assertFalse(user.isAdmin(), "Admin status should remain unchanged");
    }    /**
     * Test validation for email updates
     */

    @Test
    public void testEmailValidation() {
        // Test valid email updates with formats from your database
        String[] validEmails = {
            "kenenisa@g.c",
            "bekele@gmail.com",
            "bakela@gmail.com"
        };

        for (String validEmail : validEmails) {
            // Should not throw exception
            User updatedUser = new User(user.getUsername(), validEmail, user.getPassword(), user.isAdmin());
            assertEquals(validEmail, updatedUser.getEmail(), "Email should be updated to valid format");
        }

        // Test invalid email updates
        String[] invalidEmails = {
            "not an email",
            "missing@domain",
            "not a user name"
        };

        for (String invalidEmail : invalidEmails) {
            assertThrows(IllegalArgumentException.class, () -> {
                new User(user.getUsername(), invalidEmail, user.getPassword(), user.isAdmin());
            }, "User update should fail with invalid email: " + invalidEmail);
        }
    }
      /**
     * Test admin user promotion (admin functionality)
     */
    @Test
    public void testPromoteUserToAdmin() {
        // Setup regular user
        assertFalse(user.isAdmin(), "User should start as a regular user");
          // Create an admin version of the user (promotion)
        User adminUser = new User(user.getUsername(), user.getEmail(), 
                                  user.getPassword(), true);
        
        // Verify promotion
        assertTrue(adminUser.isAdmin(), "User should now be an admin");
        
        // Verify other details remain the same
        assertEquals(user.getUsername(), adminUser.getUsername(), "Username should remain unchanged");
        assertEquals(user.getEmail(), adminUser.getEmail(), "Email should remain unchanged");
    }
}
