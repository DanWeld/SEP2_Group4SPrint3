package persistence.daos.user;

import dtos.User;

import java.sql.SQLException;

public interface UserDAO {
    User create(String username, String email, String password) throws SQLException;
    User read(String email, String password) throws SQLException;
}
