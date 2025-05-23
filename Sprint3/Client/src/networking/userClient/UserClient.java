package networking.userClient;

import dtos.User;
import observer.PropertyChangeSubject;

public interface UserClient  extends PropertyChangeSubject
{
  void getAllUsers();
  void promoteToAdmin(String username);
  void updateUser(User user);
  void deleteUser(String username);
}
