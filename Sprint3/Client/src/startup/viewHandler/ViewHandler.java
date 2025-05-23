package startup.viewHandler;

import dtos.Booking;
import dtos.BookingHistory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dtos.Property;
import startup.ViewModelFactory;
import ui.booking.BookingController;
import ui.booking.BookingVM;
import ui.currentBookingList.CurrentBookingListCtrl;

import ui.extendBooking.ExtendBookingVM;
import ui.futureBookingList.FutureBookingListCtrl;
import ui.pastBookingList.PastBookingListCtrl;
import ui.dashboard.AdminDashboardCtrl;
import ui.dashboard.UserDashboardCtrl;
import ui.login.LoginCtrl;
import ui.propertyList.PropertyListController;
import ui.propertyList.PropertyListVM;
import ui.register.RegisterCtrl;
import ui.specifyDates.SpecifyDatesController;
import ui.welcome.FrontViewCtrl;

import java.sql.Date;

public class ViewHandler
{
  private final Stage mainStage;
  private final ViewModelFactory viewModelFactory;
  private PropertyListVM propertyListVM;
  private BookingVM bookingVM;
  private ExtendBookingVM extendBookingVM;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    propertyListVM = viewModelFactory.getPropertyListVM();
    bookingVM = viewModelFactory.getBookingVM();
    extendBookingVM = viewModelFactory.getExtendBookingVM();
    mainStage = new Stage();
  }

  public void start()
  {
    showView(ViewType.WELCOME);
    mainStage.show();
  }

  public void showView(ViewType view)
  {
    try
    {
      switch (view)
      {
        case WELCOME -> showFrontView();
        case REGISTER -> showRegisterView();
        case LOGIN -> showLoginView();
        case PROPERTY_LIST -> openPropertyListView();
        case BOOKING -> openBookingView();
        case SPECIFY_DATES -> openSpecifyDatesView();
        case USER_DASHBOARD -> showUserDashboardView();
        case ADMIN_DASHBOARD -> showAdminDashboardView();
        case PAST_BOOKINGS -> showPastBookingsView();
        case CURRENT_BOOKINGS -> showCurrentBookingsView();
        case FUTURE_BOOKINGS -> showFutureBookingsView();
        case EXTEND_BOOKING -> openExtendBookingView();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private Scene frontScene;

  public void showFrontView() throws Exception
  {
    if (frontScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          ViewHandler.class.getResource("/ui/welcome/welcomeView.fxml"));
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/register/RegisterView.fxml"));
      Parent root = loader.load();
      RegisterCtrl registerController = loader.getController();
      registerController.initialize(viewModelFactory.getRegisterVM(), this);
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
      loader.setLocation(
          getClass().getClassLoader().getResource("ui/login/LoginView.fxml"));
      Parent root = loader.load();
      LoginCtrl loginController = loader.getController();
      loginController.initialize(viewModelFactory.getLoginVM(), this);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader()
            .getResource("ui/specifyDates/SpecifyDates.fxml"));
        Parent root = loader.load();
        SpecifyDatesController specifyDatesController = loader.getController();
        specifyDatesController.initialize(viewModelFactory.getSpecifyDatesVM(),
            this);
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

  private Scene propertyListScene;

  public void openPropertyListView()
  {
    try
    {
      if (propertyListScene == null)
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader()
            .getResource("ui/propertyList/PropertyList.fxml"));
        Parent root = loader.load();
        PropertyListController propertyListController = loader.getController();
        propertyListController.initialize(viewModelFactory.getPropertyListVM(),
            this);
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

  public void openBookingView() throws Exception
  {
    try
    {
      if (bookingScene == null)
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
            getClass().getClassLoader().getResource("ui/booking/Booking.fxml"));
        Parent root = loader.load();
        BookingController bookingController = loader.getController();

        bookingController.initialize(viewModelFactory.getBookingVM(), this);
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/dashboard/userDashboard.fxml"));
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/dashboard/adminDashboard.fxml"));
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/pastBookingList/PastBookingList.fxml"));
      Parent root = loader.load();
      PastBookingListCtrl controller = loader.getController();
      controller.initialize(viewModelFactory.getBookingHistoryVM(), this);
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/currentBookingList/CurrentBookingList.fxml"));
      Parent root = loader.load();
      CurrentBookingListCtrl controller = loader.getController();
      controller.initialize(viewModelFactory.getCurrentBookingListVM(), this);
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
      loader.setLocation(getClass().getClassLoader()
          .getResource("ui/futureBookingList/FutureBookingList.fxml"));
      Parent root = loader.load();
      FutureBookingListCtrl controller = loader.getController();
      controller.initialize(viewModelFactory.getFutureBookingListVM(), this);
      futureBookingsScene = new Scene(root);
    }
    mainStage.setTitle("Booking History");
    mainStage.setScene(futureBookingsScene);
  }

  private Scene extendBookingScene;

  public void openExtendBookingView() throws Exception
  {
    if (extendBookingScene == null)
    {
      try
      {
        // Create a new scene each time to avoid stale data
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader()
            .getResource("ui/extendBooking/ExtendBooking.fxml"));
        Parent root = loader.load();
        ui.extendBooking.ExtendBookingCtrl controller = loader.getController();

        // Pass the booking to the controller
        controller.initialize(viewModelFactory.getExtendBookingVM(), this);

        extendBookingScene = new Scene(root);
        mainStage.setTitle("Extend Booking");
        mainStage.setScene(extendBookingScene);
      }
      catch (Exception e)
      {
        System.err.println(
            "Error showing extend booking view: " + e.getMessage());
        e.printStackTrace();
        // Show a default view or error message
        showUserDashboardView(); // Fallback to dashboard
      }
    }
  }

  public void setDates(Date startDate, Date endDate)
  {
    propertyListVM.setDates(startDate, endDate);
    bookingVM.setDates(startDate, endDate);
  }

  public void setProperty(Property property)
  {
    bookingVM.setProperty(property);
  }

  public void setBooking(BookingHistory booking)
  {
    extendBookingVM.setBooking(booking);
  }

  public enum ViewType
  {
    WELCOME, REGISTER, LOGIN, PROPERTY_LIST, BOOKING, SPECIFY_DATES, USER_DASHBOARD, ADMIN_DASHBOARD, PAST_BOOKINGS, CURRENT_BOOKINGS, FUTURE_BOOKINGS, EXTEND_BOOKING
  }
}
