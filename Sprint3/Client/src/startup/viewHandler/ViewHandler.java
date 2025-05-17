package startup.viewHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dtos.Property;
import startup.ViewModelFactory;
import ui.booking.BookingController;
import ui.booking.BookingVM;
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
import ui.userList.UserListCtrl;
import ui.userList.UserListVM;
import ui.welcome.FrontViewCtrl;

import java.sql.Date;

public class ViewHandler
{
  private final SpecifyDatesVM specifyDatesVM;
  private final PropertyListVM propertyListVM;
  private final BookingVM bookingVM;
  private final RegisterVM registerVM;
  private final LoginVM loginVM;
  private final Stage mainStage;
  private UserListVM userListVM;


  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    specifyDatesVM = viewModelFactory.getSpecifyDatesVM();
    propertyListVM = viewModelFactory.getPropertyListVM();
    bookingVM = viewModelFactory.getBookingVM();
    registerVM = viewModelFactory.getRegisterVM();
    loginVM = viewModelFactory.getLoginVM();
    userListVM = viewModelFactory.getUserGuiVM();
    mainStage = new Stage();
  }

  public void start()
  {
    showView(ViewType.Manage_Users);
    mainStage.show();
  }

  public void showView(ViewType view){
    try{
      switch (view){
        case WELCOME -> showFrontView();
        case REGISTER -> showRegisterView();
        case LOGIN -> showLoginView();
        case PROPERTY_LIST -> openPropertyListView();
        case BOOKING -> openBookingView(propertyListVM.getSelectedProperty().get());
        case SPECIFY_DATES -> openSpecifyDatesView();
        case USER_DASHBOARD -> showUserDashboardView();
        case ADMIN_DASHBOARD -> showAdminDashboardView();
        case Manage_Users -> showManageUsersView();
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
      loader.setLocation(ViewHandler.class.getResource("/ui/userList/UserList.fxml"));
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
          "ui/register/registerView.fxml"));
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
          "ui/login/LoginCtrl.fxml"));
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
  private Scene manageUsersScene;
  public void showManageUsersView() throws Exception
  {

    if (manageUsersScene == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource(
          "ui/userList/UserList.fxml"));
      Parent root = loader.load();
      UserListCtrl controller = loader.getController();
      controller.initialize(this,userListVM);
      manageUsersScene = new Scene(root);
    }
    mainStage.setTitle("Manage Users");
    mainStage.setScene(manageUsersScene);
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
    Manage_Users
  }
}
