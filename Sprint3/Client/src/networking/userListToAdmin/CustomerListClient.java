package networking.userListToAdmin;

import dtos.User;

import java.util.List;

public interface CustomerListClient
{
  /**
   * Searches for users based on username and email.
   *
   * @param username The username to search for.
   * @param email The email to search for.
   * @return A list of users matching the search criteria.
   * @throws Exception If an error occurs during the search.
   */
  List<User> searchUsers(String username, String email) throws Exception;

  /**
   * Upgrades a user to admin based on their ID.
   *
   * @param userName The ID of the user to upgrade.
   * @throws Exception If an error occurs during the upgrade.
   */
  void upgradeToAdmin(String userName) throws Exception;
}
