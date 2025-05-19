package services.user;

import dtos.User;
import observer.PropertyChangeSubject;

public interface UserService extends PropertyChangeSubject
{
  void promoteToAdmin(String username);
  void updateUser(User user);
  void deleteUser(String username);
  void getAllUsers();
}
