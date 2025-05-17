package model.authentication;

import dtos.User;
import observer.PropertyChangeSubject;

/**
 * Interface for user authentication service
 */
public interface AuthenticationService extends PropertyChangeSubject
{
    /**
     * Authenticate a user with email and password
     * @param email User's email
     * @param password User's password
     */
    void authenticate(String email, String password);

    /**
     * Register a new user
     * @param user User to register
     */
    void registerUser(User user);
}
