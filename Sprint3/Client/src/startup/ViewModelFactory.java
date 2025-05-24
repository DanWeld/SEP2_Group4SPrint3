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

/**
 * Factory class for creating view models
 */
public class ViewModelFactory
{
  public ViewModelFactory()
  {
  }

  public LoginVM getLoginVM()
  {
    return new LoginVM();
  }

  public RegisterVM getRegisterVM()
  {
    return new RegisterVM();
  }

  public SpecifyDatesVM getSpecifyDatesVM()
  {
    return new SpecifyDatesVM();
  }

  public PropertyListVM getPropertyListVM()
  {
    return new PropertyListVM();
  }

  public BookingVM getBookingVM()
  {
    return new BookingVM();
  }

  public PastBookingListVM getBookingHistoryVM()
  {
    return new PastBookingListVM();
  }

  public CurrentBookingListVM getCurrentBookingListVM()
  {
    return new CurrentBookingListVM();
  }

  public FutureBookingListVM getFutureBookingListVM()
  {
    return new FutureBookingListVM();
  }

  public ExtendBookingVM getExtendBookingVM()
  {
    return new ExtendBookingVM();
  }

  public UserListVM getUserListVM()
  {
    return new UserListVM();
  }

  public AdminPropertyListVM getAdminPropertyListVM()
  {
    return new AdminPropertyListVM();
  }

  public AdminBookingHistoryVM getAdminBookingHistoryVM()
  {
    return new AdminBookingHistoryVM();
  }

  public PropertyManagementVM getPropertyManagementVM()
  {
    return new PropertyManagementVM();
  }

  public AddPropertyVM getAddPropertyVM()
  {
    return new AddPropertyVM();
  }
}
