package networking.profileHandler;

import dtos.UserProfile;


public interface ProfileHandler {
    

    UserProfile getProfile(String username) throws Exception;

    boolean updateProfile(UserProfile profile) throws Exception;
    


    boolean deleteProfile(String username) throws Exception;

    boolean changePassword(String username, String currentPassword, String newPassword) throws Exception;
}
