package model.authentication;

import dtos.LoginRequest;
import dtos.User;

public interface AuthenticationService {
    String registerUser(User newUser);
    String loginUser(LoginRequest request);
}
