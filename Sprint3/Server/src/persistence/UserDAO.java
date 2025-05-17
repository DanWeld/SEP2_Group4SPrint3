package persistence;

import dtos.User;

import java.sql.SQLException;

public interface UserDAO {
    User create(String username, String email, String password, boolean isAdmin) throws SQLException;
    User read(String email, String password) throws SQLException;
    User readByUsername(String username, String password) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    User getUserByUsername(String username) throws SQLException;
    void update(User user) throws SQLException;
    boolean saveUser(User user);
    void updatetoAdmin(String userName) throws SQLException;
}
