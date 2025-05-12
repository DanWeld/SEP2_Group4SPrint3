package startup;

import networking.auth.Authentication;
import ui.booking.BookingVM;
import ui.currentBookingList.CurrentBookingListVM;
import ui.futureBookingList.FutureBookingListVM;
import ui.pastBookingList.PastBookingListVM;
import ui.login.LoginVM;
import ui.userProfile.UserProfileVM;
import ui.propertyList.PropertyListVM;
import ui.register.RegisterVM;
import ui.specifyDates.SpecifyDatesVM;
import ui.extendBooking.ExtendBookingVM;
import ui.adminUserList.AdminUserListVM;
import ui.adminPropertyList.AdminPropertyListVM;
import ui.adminPropertyHistory.AdminPropertyHistoryVM;

/**
 * Factory class for creating view models
 */
public class ViewModelFactory
{
  private final LoginVM loginVM;
  private final RegisterVM registerVM;
  private final SpecifyDatesVM specifyDatesVM;
  private final PropertyListVM propertyListVM;
  private final BookingVM bookingVM;
  private final PastBookingListVM pastBookingListVM;
  private final CurrentBookingListVM currentBookingListVM;
  private final FutureBookingListVM futureBookingListVM;
  private final UserProfileVM userProfileVM;
  private final ExtendBookingVM extendBookingVM;
  private final AdminUserListVM adminUserListVM;
  private final AdminPropertyListVM adminPropertyListVM;
  private final AdminPropertyHistoryVM adminPropertyHistoryVM;

  public ViewModelFactory(Authentication authService)
  {
    // Get user profile client
    ClientFactory clientFactory = ClientFactory.getInstance();
    
    loginVM = new LoginVM(authService);
    registerVM = new RegisterVM(authService);
    specifyDatesVM = new SpecifyDatesVM();
    propertyListVM = new PropertyListVM();
    bookingVM = new BookingVM();
    pastBookingListVM = new PastBookingListVM();
    currentBookingListVM = new CurrentBookingListVM();
    futureBookingListVM = new FutureBookingListVM();
    
    // Initialize userProfileVM with the profile client, user session, and booking history client
    System.out.println("DEBUG: Creating UserProfileVM with BookingHistoryClient: " + clientFactory.getBookingHistoryClient());
    userProfileVM = new UserProfileVM(
        clientFactory.getUserProfileClient(),
        services.UserSession.getInstance(),
        clientFactory.getBookingHistoryClient()
    );
    
    // Initialize extendBookingVM with the booking client
    extendBookingVM = new ExtendBookingVM(
        clientFactory.getBookingClient()
    );
    
    adminUserListVM = new AdminUserListVM();
    adminPropertyListVM = new AdminPropertyListVM();
    adminPropertyHistoryVM = new AdminPropertyHistoryVM();
  }

  public LoginVM getLoginVM()
  {
    return loginVM;
  }

  public RegisterVM getRegisterVM()
  {
    return registerVM;
  }

  public SpecifyDatesVM getSpecifyDatesVM()
  {
    return specifyDatesVM;
  }

  public PropertyListVM getPropertyListVM()
  {
    return propertyListVM;
  }

  public BookingVM getBookingVM()
  {
    return bookingVM;
  }

  public PastBookingListVM getBookingHistoryVM()
  {
    return pastBookingListVM;
  }

  public CurrentBookingListVM getCurrentBookingListVM()
  {
    return currentBookingListVM;
  }

  public FutureBookingListVM getFutureBookingListVM()
  {
    return futureBookingListVM;
  }

  public UserProfileVM getUserProfileVM() {
    return userProfileVM;
  }

  public ExtendBookingVM getExtendBookingVM() {
    return extendBookingVM;
  }

  public AdminUserListVM getAdminUserListVM() {
    return adminUserListVM;
  }

  public AdminPropertyListVM getAdminPropertyListVM() {
    return adminPropertyListVM;
  }

  public AdminPropertyHistoryVM getAdminPropertyHistoryVM() {
    return adminPropertyHistoryVM;
  }
}
