package ui.booking;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import startup.viewHandler.ViewHandler;

/**
 * The BookingController class is responsible for handling the user interface
 * interactions related to booking a property. It binds the UI components to the
 * ViewModel and manages the actions performed by the user.
 *
 * @author Group 4
 * @version 1.0
 */
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

  /**
   * Default constructor for BookingController.
   */
  public BookingController()
  {
  }

  /**
   * Initializes the BookingController with the provided BookingVM and ViewHandler.
   *
   * @param bookingVM  The ViewModel for booking.
   * @param viewHandler The ViewHandler for managing views.
   */
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

  /**
   * Sets the booking date in the ViewModel when the user selects a new date.
   * Called when the user changes the date in the DatePicker.
   */
  public void changeEndDate()
  {
    bookingVM.onChangeEndDate(bookingDateField.getValue());
    newEndDateAvailabilityField.textProperty().bind(bookingVM.getAvailabilityProperty());
  }

  /**
   * Back button action handler.
   * From the BookingController, returns to the PropertyListView.
   * Called when the user changes the date in the DatePicker.
   */
  public void onBackButtonClicked()
  {
    viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
  }

  /**
   * Submit button action handler.
   * From the BookingController, creates a booking.
   * Called when the user clicks the submit button.
   */
  public void onSubmitButtonClicked()
  {
    bookingVM.createBooking();
  }
}