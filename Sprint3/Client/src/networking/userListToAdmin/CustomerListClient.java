package networking.userListToAdmin;

import dtos.User;

import java.util.List;

public interface CustomerListClient
{

  /**
   * Upgrades a user to admin based on their ID.
   *
   * @param userName The ID of the user to upgrade.
   * @throws Exception If an error occurs during the upgrade.
   */
  void upgradeToAdmin(String userName) throws Exception;
}
