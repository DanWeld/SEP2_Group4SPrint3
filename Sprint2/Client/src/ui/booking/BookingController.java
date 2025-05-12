package Client.src.ui.booking;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ui.viewHandler.ViewHandler;

public class BookingController
{
  //I want to add an image to the property
  @FXML private TextArea locationTextArea;
  @FXML private TextArea pricePerNightField;
  @FXML private TextArea facilitiesTextField;
  @FXML private DatePicker bookingDateField;
  @FXML private TextArea newEndDateAvailabilityField;
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Label errorMsg;

  private BookingVM bookingVM;
  private ViewHandler viewHandler;

  public BookingController()
  {
  }

  public void initialize(BookingVM bookingVM, ViewHandler viewHandler)
  {
    this.bookingVM = bookingVM;
    this.viewHandler = viewHandler;

    locationTextArea.textProperty().bind(bookingVM.getLocationProperty());
    pricePerNightField.textProperty().bind(bookingVM.getPricePerNightProperty().asString());
    facilitiesTextField.textProperty().bind(bookingVM.getFacilitiesProperty());
    newEndDateAvailabilityField.setEditable(false);
    errorMsg.textProperty().bind(bookingVM.getErrorMsgProperty());
    bookingDateField.setValue(bookingVM.getEndDate().toLocalDate());
  }

  public void changeEndDate()
  {
    bookingVM.onChangeEndDate(bookingDateField.getValue());
    newEndDateAvailabilityField.textProperty().bind(bookingVM.getAvailabilityProperty());
  }

  public void onBackButtonClicked()
  {
    viewHandler.openPropertyListView();
  }

  public void onSubmitButtonClicked()
  {
    bookingVM.createBooking();
  }
}