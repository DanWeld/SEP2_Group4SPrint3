package startup;

import networking.auth.Authentication;
import ui.booking.BookingVM;
import ui.login.LoginVM;
import ui.propertyList.PropertyListVM;
import ui.register.RegisterVM;
import ui.specifyDates.SpecifyDatesVM;
import ui.userToAdminUi.UserGuiVM;

/**
 * Factory class for creating view models
 */
public class ViewModelFactory {
    private final LoginVM loginVM;
    private final RegisterVM registerVM;
    private final SpecifyDatesVM specifyDatesVM;
    private final PropertyListVM propertyListVM;
    private final BookingVM bookingVM;
    private final UserGuiVM userGuiVM;
    
    public ViewModelFactory(Authentication authService) {
        loginVM = new LoginVM(authService);
        registerVM = new RegisterVM(authService);
        specifyDatesVM = new SpecifyDatesVM();
        propertyListVM = new PropertyListVM();
        bookingVM = new BookingVM();
        userGuiVM = new UserGuiVM();
    }
    
    public LoginVM getLoginVM() {
        return loginVM;
    }
    
    public RegisterVM getRegisterVM() {
        return registerVM;
    }
    
    public SpecifyDatesVM getSpecifyDatesVM() {
        return specifyDatesVM;
    }
    
    public PropertyListVM getPropertyListVM() {
        return propertyListVM;
    }
    
    public BookingVM getBookingVM() {
        return bookingVM;
    }

    public UserGuiVM getUserGuiVM(){return userGuiVM;}
}
