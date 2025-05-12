package startup.viewHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dtos.Property;
import dtos.BookingHistory;
import dtos.Facilities;
import startup.ViewModelFactory;
import ui.booking.BookingController;
import ui.booking.BookingVM;
import ui.currentBookingList.CurrentBookingListCtrl;
import ui.currentBookingList.CurrentBookingListVM;
import ui.futureBookingList.FutureBookingListCtrl;
import ui.futureBookingList.FutureBookingListVM;
import ui.pastBookingList.PastBookingListCtrl;
import ui.pastBookingList.PastBookingListVM;
import ui.userProfile.UserProfileVM;
import ui.dashboard.AdminDashboardCtrl;
import ui.dashboard.UserDashboardCtrl;
import ui.login.LoginCtrl;
import ui.login.LoginVM;
import ui.propertyList.PropertyListController;
import ui.propertyList.PropertyListVM;
import ui.register.RegisterCtrl;
import ui.register.RegisterVM;
import ui.specifyDates.SpecifyDatesController;
import ui.specifyDates.SpecifyDatesVM;
import ui.welcome.FrontViewCtrl;

import java.sql.Date;

public class ViewHandler
{
  private final SpecifyDatesVM specifyDatesVM;
  private final PropertyListVM propertyListVM;
  private final BookingVM bookingVM;
  private final RegisterVM registerVM;
  private final PastBookingListVM pastBookingListVM;
  private final CurrentBookingListVM currentBookingListVM;
  private final FutureBookingListVM futureBookingListVM;
  private final LoginVM loginVM;
  private final Stage mainStage;
  private final ViewModelFactory viewModelFactory;


  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    specifyDatesVM = viewModelFactory.getSpecifyDatesVM();
    propertyListVM = viewModelFactory.getPropertyListVM();
    bookingVM = viewModelFactory.getBookingVM();
    registerVM = viewModelFactory.getRegisterVM();
    loginVM = viewModelFactory.getLoginVM();
    pastBookingListVM = viewModelFactory.getBookingHistoryVM();
    currentBookingListVM = viewModelFactory.getCurrentBookingListVM();
    futureBookingListVM = viewModelFactory.getFutureBookingListVM();

    mainStage = new Stage();
  }

  public void start()
  {
    showView(ViewType.WELCOME);
    mainStage.show();
  }

  public void showView(ViewType view){
    try{
      System.out.println("DEBUG: Navigating to view: " + view);
      switch (view){
        case WELCOME -> showFrontView();
        case REGISTER -> showRegisterView();
        case LOGIN -> showLoginView();
        case PROPERTY_LIST -> openPropertyListView();
        case BOOKING -> openBookingView(propertyListVM.getSelectedProperty().get());
        case SPECIFY_DATES -> openSpecifyDatesView();
        case USER_DASHBOARD -> showUserDashboardView();
        case ADMIN_DASHBOARD -> showAdminDashboardView();
        case PAST_BOOKINGS -> showPastBookingsView();
        case CURRENT_BOOKINGS -> showCurrentBookingsView();
        case FUTURE_BOOKINGS -> showFutureBookingsView();
        case USER_PROFILE -> showUserProfileView();
        case EXTEND_BOOKING -> showExtendBookingView();
        case ADMIN_USER_LIST -> showAdminUserListView();
        case ADMIN_PROPERTY_LIST -> showAdminPropertyListView();
        case ADMIN_PROPERTY_HISTORY -> showAdminPropertyHistoryView();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Scene frontScene;
  public void showFrontView() throws Exception
  {
    if (frontScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(ViewHandler.class.getResource("/ui/welcome/welcomeView.fxml"));
      Parent root = loader.load();
      FrontViewCtrl frontViewController = loader.getController();
      frontViewController.initialize(this);
      frontScene = new Scene(root);
    }
    mainStage.setTitle("Welcome");
    mainStage.setScene(frontScene);
  }

  private Scene registerScene;
  public void showRegisterView() throws Exception
  {
    if (registerScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/register/RegisterView.fxml"));
      Parent root = loader.load();
      RegisterCtrl registerController = loader.getController();
      registerController.initialize(registerVM, this);
      registerScene = new Scene(root);
    }
    mainStage.setTitle("Register");
    mainStage.setScene(registerScene);
  }

  private Scene loginScene;
  public void showLoginView() throws Exception
  {
    if (loginScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/login/LoginView.fxml"));
      Parent root = loader.load();
      LoginCtrl loginController = loader.getController();
      loginController.initialize(loginVM, this);
      loginScene = new Scene(root);
    }
    mainStage.setTitle("Login");
    mainStage.setScene(loginScene);
  }

  private Scene specifyDatesScene;
  public void openSpecifyDatesView()
  {
    try
    {
      if (specifyDatesScene == null)
      {
        FXMLLoader loader = new FXMLLoader();      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/specifyDates/SpecifyDates.fxml"));
      Parent root = loader.load();
        SpecifyDatesController specifyDatesController = loader.getController();
        specifyDatesController.initialize(specifyDatesVM, this);
        specifyDatesScene = new Scene(root);
      }
      mainStage.setTitle("Specify Dates");
      mainStage.setScene(specifyDatesScene);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void setDates(Date startDate, Date endDate)
  {
    propertyListVM.setDates(startDate, endDate);
    bookingVM.setDates(startDate, endDate);
  }

  private Scene propertyListScene;
  public void openPropertyListView()
  {
    try
    {
      if (propertyListScene == null)
      {
        FXMLLoader loader = new FXMLLoader();      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/propertyList/PropertyList.fxml"));
      Parent root = loader.load();
        PropertyListController propertyListController = loader.getController();
        propertyListController.initialize(propertyListVM, this);
        propertyListScene = new Scene(root);
      }
      mainStage.setTitle("Property List");
      mainStage.setScene(propertyListScene);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private Scene bookingScene;
  public void openBookingView(Property property) throws Exception
  {
    bookingVM.updateProperty(property);
    try
    {
      if (bookingScene == null)
      {
        FXMLLoader loader = new FXMLLoader();      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/booking/Booking.fxml"));
      Parent root = loader.load();
        BookingController bookingController = loader.getController();

        bookingController.initialize(bookingVM, this);
        bookingScene = new Scene(root);
      }
      mainStage.setTitle("Booking");
      mainStage.setScene(bookingScene);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private Scene userDashboardScene;
  public void showUserDashboardView() throws Exception
  {
    if (userDashboardScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/dashboard/userDashboard.fxml"));
      Parent root = loader.load();
      UserDashboardCtrl controller = loader.getController();
      controller.initialize(this);
      userDashboardScene = new Scene(root);
    }
    mainStage.setTitle("User Dashboard");
    mainStage.setScene(userDashboardScene);
  }

  private Scene adminDashboardScene;
  public void showAdminDashboardView() throws Exception
  {
    if (adminDashboardScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/dashboard/adminDashboard.fxml"));
      Parent root = loader.load();
      AdminDashboardCtrl controller = loader.getController();
      controller.initialize(this);
      adminDashboardScene = new Scene(root);
    }
    mainStage.setTitle("Admin Dashboard");
    mainStage.setScene(adminDashboardScene);
  }

  private Scene pastBookingsScene;
  public void showPastBookingsView() throws Exception
  {
    if (pastBookingsScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/pastBookingList/PastBookingList.fxml"));
      Parent root = loader.load();
      PastBookingListCtrl controller = loader.getController();
      controller.initialize(pastBookingListVM, this);
      pastBookingsScene = new Scene(root);
    }
    mainStage.setTitle("Booking History");
    mainStage.setScene(pastBookingsScene);
  }

  private Scene currentBookingsScene;
  public void showCurrentBookingsView() throws Exception
  {
    if (currentBookingsScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/currentBookingList/CurrentBookingList.fxml"));
      Parent root = loader.load();
      CurrentBookingListCtrl controller = loader.getController();
      controller.initialize(currentBookingListVM, this);
      currentBookingsScene = new Scene(root);
    }
    mainStage.setTitle("Booking History");
    mainStage.setScene(currentBookingsScene);
  }

  private Scene futureBookingsScene;
  public void showFutureBookingsView() throws Exception
  {
    if (futureBookingsScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/futureBookingList/FutureBookingList.fxml"));
      Parent root = loader.load();
      FutureBookingListCtrl controller = loader.getController();
      controller.initialize(futureBookingListVM, this);
      futureBookingsScene = new Scene(root);
    }
    mainStage.setTitle("Booking History");
    mainStage.setScene(futureBookingsScene);
  }

  // This method is now deprecated in favor of the newer implementation below
  // The actual implementation is at line ~394
  public void showUserProfileViewOld() throws Exception
  {
    showUserProfileView(); // Delegate to the new implementation
  }

  private Scene extendBookingScene;
  public void showExtendBookingView() throws Exception {
    showExtendBookingView(null); // Call the overloaded method with null
  }
  
  public void showExtendBookingView(BookingHistory booking) throws Exception {
    try {
      // Create a new scene each time to avoid stale data
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/extendBooking/ExtendBooking.fxml"));
      Parent root = loader.load();
      ui.extendBooking.ExtendBookingCtrl controller = loader.getController();
      
      // Pass the booking to the controller
      controller.initialize(viewModelFactory.getExtendBookingVM(), this, booking);
      
      extendBookingScene = new Scene(root);
      mainStage.setTitle("Extend Booking");
      mainStage.setScene(extendBookingScene);
    } catch (Exception e) {
      System.err.println("Error showing extend booking view: " + e.getMessage());
      e.printStackTrace();
      // Show a default view or error message
      showUserDashboardView(); // Fallback to dashboard
    }
  }

  private Scene adminUserListScene;
  public void showAdminUserListView() throws Exception
  {
    if (adminUserListScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/adminUserList/AdminUserList.fxml"));
      Parent root = loader.load();
      ui.adminUserList.AdminUserListCtrl controller = loader.getController();
      controller.initialize(viewModelFactory.getAdminUserListVM(), this);
      adminUserListScene = new Scene(root);
    }
    mainStage.setTitle("Manage Users");
    mainStage.setScene(adminUserListScene);
  }

  private Scene adminPropertyListScene;
  public void showAdminPropertyListView() throws Exception
  {
    if (adminPropertyListScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/adminPropertyList/AdminPropertyList.fxml"));
      Parent root = loader.load();
      ui.adminPropertyList.AdminPropertyListCtrl controller = loader.getController();
      controller.initialize(viewModelFactory.getAdminPropertyListVM(), this);
      adminPropertyListScene = new Scene(root);
    }
    mainStage.setTitle("Manage Properties");
    mainStage.setScene(adminPropertyListScene);
  }

  private Scene adminPropertyHistoryScene;
  public void showAdminPropertyHistoryView() throws Exception
  {
    // Note: In a real implementation, this would also pass the selected property
    // For now, we're not actually passing a property to view history for
    if (adminPropertyHistoryScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/adminPropertyHistory/AdminPropertyHistory.fxml"));
      Parent root = loader.load();
      ui.adminPropertyHistory.AdminPropertyHistoryCtrl controller = loader.getController();
      
      // Create a dummy property for development/testing purposes
      Facilities dummyFacilities = new Facilities(true, true, true, true, false);
      Property dummyProperty = new Property(
          1, "Demo Property", 100.0, true, dummyFacilities
      );
      
      // Initialize the controller with the dummy property
      controller.initialize(viewModelFactory.getAdminPropertyHistoryVM(), this, dummyProperty);
      
      adminPropertyHistoryScene = new Scene(root);
    }
    mainStage.setTitle("Property Booking History");
    mainStage.setScene(adminPropertyHistoryScene);
  }
  
  private Scene userProfileScene;
  public void showUserProfileView() throws Exception
  {
    if (userProfileScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/userProfile/UserProfileView.fxml"));
      Parent root = loader.load();
      ui.userProfile.UserProfileController controller = loader.getController();
      
      // Get the VM and set the ViewHandler
      UserProfileVM viewModel = viewModelFactory.getUserProfileVM();
      viewModel.setViewHandler(this);
      
      controller.init(viewModel);
      userProfileScene = new Scene(root);
    }
    mainStage.setTitle("User Profile");
    mainStage.setScene(userProfileScene);
  }

  public enum ViewType {
    WELCOME,
    REGISTER,
    LOGIN,
    PROPERTY_LIST,
    BOOKING,
    SPECIFY_DATES,
    USER_DASHBOARD,
    ADMIN_DASHBOARD,
    PAST_BOOKINGS,
    CURRENT_BOOKINGS,
    FUTURE_BOOKINGS,
    USER_PROFILE,
    EXTEND_BOOKING,
    ADMIN_USER_LIST,
    ADMIN_PROPERTY_LIST,
    ADMIN_PROPERTY_HISTORY
  }
}
