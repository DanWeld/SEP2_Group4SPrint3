package startup;

import networking.authClient.Authentication;
import ui.booking.BookingVM;
import ui.currentBookingList.CurrentBookingListVM;
import ui.futureBookingList.FutureBookingListVM;
import ui.pastBookingList.PastBookingListVM;
import ui.login.LoginVM;
import ui.propertyList.PropertyListVM;
import ui.register.RegisterVM;
import ui.specifyDates.SpecifyDatesVM;

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

  public ViewModelFactory(Authentication authService)
  {
    loginVM = new LoginVM(authService);
    registerVM = new RegisterVM(authService);
    specifyDatesVM = new SpecifyDatesVM();
    propertyListVM = new PropertyListVM();
    bookingVM = new BookingVM();
    pastBookingListVM = new PastBookingListVM();
    currentBookingListVM = new CurrentBookingListVM();
    futureBookingListVM = new FutureBookingListVM();
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
}
