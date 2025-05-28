package ui.booking;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import startup.viewHandler.ViewHandler;

import java.time.LocalDate;

/**
 * Controller for the Booking view.
 */
public class BookingController
{
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
   * Initializes the controller without any parameters.
   */
  public BookingController() {}

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler.
   * Binds the UI components to the ViewModel properties and sets up listeners.
   *
   * @param bookingVM the ViewModel for this view
   * @param viewHandler the ViewHandler for navigating between views
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
    submitButton.disableProperty().bind(bookingVM.getSubmitButtonDisabledProperty());

    // Setup DatePicker value to match ViewModel's end date
    bookingDateField.setValue(bookingVM.getEndDateProperty().get().toLocalDate());

    bookingDateField.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        bookingVM.onChangeEndDate(newVal);
      }
    });

    // Update DatePicker when ViewModel's endDate changes
    bookingVM.getEndDateProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null && !bookingDateField.getValue().equals(newVal.toLocalDate())) {
        bookingDateField.setValue(newVal.toLocalDate());
      }
    });

    // Availability text
    newEndDateAvailabilityField.textProperty().bind(bookingVM.getAvailabilityProperty());
  }

  /**
   * Back button click handler.
   * Navigates back to the Property List view.
   * This method is called when the back button is clicked.
   */
  @FXML
  public void onBackButtonClicked()
  {
    viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
  }

  /**
   * Submit button click handler.
   * Calls the ViewModel to create a booking.
   * This method is called when the submit button is clicked.
   */
  @FXML
  public void onSubmitButtonClicked()
  {
    bookingVM.createBooking();
  }
}
