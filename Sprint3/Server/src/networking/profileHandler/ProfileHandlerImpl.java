package networking.profileHandler;

import dtos.UserProfile;
import services.profile.UserProfileService;
import services.profile.UserProfileServiceImpl;

/**
 * Implementation of the ProfileHandler interface
 */
public class ProfileHandlerImpl implements ProfileHandler {
    private UserProfileService profileService;
    
    public ProfileHandlerImpl() throws Exception {
        this.profileService = new UserProfileServiceImpl();
    }
    
    @Override
    public UserProfile getProfile(String username) throws Exception {
        return profileService.getProfile(username);
    }
    
    @Override
    public boolean updateProfile(UserProfile profile) throws Exception {
        return profileService.updateProfile(profile);
    }
    
    @Override
    public boolean deleteProfile(String username) throws Exception {
        return profileService.deleteProfile(username);
    }
    
    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) throws Exception {
        return profileService.changePassword(username, currentPassword, newPassword);
    }
}
