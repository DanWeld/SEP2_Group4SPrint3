package Client.src.ui.viewHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dtos.Property;
import ui.booking.BookingController;
import ui.booking.BookingVM;
import ui.propertyList.PropertyListController;
import ui.propertyList.PropertyListVM;
import ui.specifyDates.SpecifyDatesController;
import ui.specifyDates.SpecifyDatesVM;

import java.sql.Date;

public class ViewHandler
{
  private SpecifyDatesVM specifyDatesVM;
  private PropertyListVM propertyListVM;
  private BookingVM bookingVM;
  private Stage mainStage;


  public ViewHandler()
  {
    specifyDatesVM = new SpecifyDatesVM();
    propertyListVM = new PropertyListVM();
    bookingVM = new BookingVM();
    mainStage = new Stage();
  }

  public void start()
  {
    openSpecifyDatesView();
    mainStage.show();
  }

  private Scene specifyDatesScene;
  public void openSpecifyDatesView()
  {
    try
    {
      if (specifyDatesScene == null)
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
            "/ui/specifyDates/SpecifyDates.fxml"));
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
            "/ui/propertyList/PropertyList.fxml"));
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
            "/ui/booking/Booking.fxml"));
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
}
