package main.dk.via.pro2.sprint1.view;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import main.dk.via.pro2.sprint1.viewmodel.BookingVM;
import main.dk.via.pro2.sprint1.viewmodel.ViewHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

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
  }

  public void changeEndDate()
  {
    bookingVM.changeEndDate(bookingDateField.getValue());
    newEndDateAvailabilityField.textProperty().bind(bookingVM.getAvailabilityProperty());
  }

  public void onBackButtonClicked()
  {
    viewHandler.openPropertyListView();
  }

  public void onSubmitButtonClicked()
  {
    // Implement the logic to handle the submit button click (Implementation of Sprint 2)
    // Handle the submit button click
  }
}