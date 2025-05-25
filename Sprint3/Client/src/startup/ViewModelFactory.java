package startup;

import networking.authClient.Authentication;
import ui.addProperty.AddPropertyVM;
import ui.adminBookingHistory.AdminBookingHistoryVM;
import ui.adminPropertyList.AdminPropertyListVM;
import ui.booking.BookingVM;
import ui.currentBookingList.CurrentBookingListVM;
import ui.extendBooking.ExtendBookingVM;
import ui.futureBookingList.FutureBookingListVM;
import ui.pastBookingList.PastBookingListVM;
import ui.login.LoginVM;
import ui.propertyList.PropertyListVM;
import ui.propertyManagement.PropertyManagementVM;
import ui.register.RegisterVM;
import ui.specifyDates.SpecifyDatesVM;
import ui.userList.UserListVM;
import ui.userProfile.UserProfileVM;

/**
 * Factory class for creating view models
 */
public class ViewModelFactory
{
  /**
   * Default constructor for ViewModelFactory
   * Initializes the factory without any parameters
   */
  public ViewModelFactory()
  {
  }

  /**
   * get Login ViewModel
   *
   * @return LoginVM instance
   */
  public LoginVM getLoginVM()
  {
    return new LoginVM();
  }

  /**
   * get Register ViewModel
   *
   * @return RegisterVM instance
   */
  public RegisterVM getRegisterVM()
  {
    return new RegisterVM();
  }

  /**
   * get SpecifyDates ViewModel
   *
   * @return SpecifyDatesVM instance
   */
  public SpecifyDatesVM getSpecifyDatesVM()
  {
    return new SpecifyDatesVM();
  }

  /**
   * get Authentication service
   *
   * @return Authentication instance
   */
  public PropertyListVM getPropertyListVM()
  {
    return new PropertyListVM();
  }

  /**
   * get Booking ViewModel
   *
   * @return BookingVM instance
   */
  public BookingVM getBookingVM()
  {
    return new BookingVM();
  }

  /**
   * get PastBookingList ViewModel
   *
   * @return PastBookingListVM instance
   */
  public PastBookingListVM getBookingHistoryVM()
  {
    return new PastBookingListVM();
  }

  /**
   * get CurrentBookingList ViewModel
   *
   * @return CurrentBookingListVM instance
   */
  public CurrentBookingListVM getCurrentBookingListVM()
  {
    return new CurrentBookingListVM();
  }

  /**
   * get FutureBookingList ViewModel
   *
   * @return FutureBookingListVM instance
   */
  public FutureBookingListVM getFutureBookingListVM()
  {
    return new FutureBookingListVM();
  }

  /**
   * get ExtendBooking ViewModel
   *
   * @return ExtendBookingVM instance
   */
  public ExtendBookingVM getExtendBookingVM()
  {
    return new ExtendBookingVM();
  }

  /**
   * get AdminPropertyList ViewModel
   *
   * @return AdminPropertyListVM instance
   */
  public UserListVM getUserListVM()
  {
    return new UserListVM();
  }

  /**
   * get AdminPropertyList ViewModel
   *
   * @return AdminPropertyListVM instance
   */
  public AdminPropertyListVM getAdminPropertyListVM()
  {
    return new AdminPropertyListVM();
  }

  /**
   * get AdminPropertyHistory ViewModel
   *
   * @return AdminPropertyHistoryVM instance
   */
  public AdminBookingHistoryVM getAdminBookingHistoryVM()
  {
    return new AdminBookingHistoryVM();
  }

  /**
   * get UserProfile ViewModel
   *
   * @return UserProfileVM instance
   */
  public PropertyManagementVM getPropertyManagementVM()
  {
    return new PropertyManagementVM();
  }

  /**
   * get AddProperty ViewModel
   *
   * @return AddPropertyVM instance
   */
  public AddPropertyVM getAddPropertyVM()
  {
    return new AddPropertyVM();
  }

  /**
   * get UserProfile ViewModel
   *
   * @return UserProfileVM instance
   */
  public UserProfileVM getUserProfileVM()
  {
    return new UserProfileVM();
  }
}
