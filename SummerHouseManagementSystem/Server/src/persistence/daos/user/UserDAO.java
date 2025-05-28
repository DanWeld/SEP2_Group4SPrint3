package persistence.daos.user;

import dtos.User;

import java.sql.SQLException;
import java.util.List;

/**
 * UserDAO interface for managing user data in the system.
 * Provides methods for creating, reading, updating, deleting users,
 * promoting users to admin, and retrieving all users.
 */
public interface UserDAO {
    User create(String username, String email, String password) throws SQLException;
    User read(String email, String password) throws SQLException;
    void update(User user) throws SQLException;
    void delete(String username) throws SQLException;
    void promoteToAdmin(String username) throws SQLException;
    List<User> getAllUsers() throws SQLException;
}
