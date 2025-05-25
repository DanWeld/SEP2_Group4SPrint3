package ui.specifyDates;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

import java.sql.Date;

/**
 * Class to control the Specify Dates view.
 * This class handles the user input for specifying the start and end dates
 * for a booking and validates the input before proceeding.
 * * @author Group 4
 * * @version 1.0
 */
public class SpecifyDatesCtrl
{
  private @FXML DatePicker startDate;
  private @FXML DatePicker endDate;
  private @FXML Label errMsg;
  private @FXML Button proceedButton;
  private @FXML Button backButton;

  private SpecifyDatesVM specifyDatesVM;
  private ViewHandler viewHandler;

  /**
   * Constructor for SpecifyDatesCtrl.
   * Initializes the controller for the Specify Dates view.
   */
  public SpecifyDatesCtrl()
  {
  }

  /**
   * Initializes the SpecifyDatesCtrl with the ViewModel and ViewHandler.
   * This method is called by the JavaFX framework to initialize the controller.
   *
   * @param specifyDatesVM The ViewModel for the Specify Dates view.
   * @param viewHandler    The ViewHandler for handling view changes.
   */
  public void initialize(SpecifyDatesVM specifyDatesVM, ViewHandler viewHandler)
  {
    this.specifyDatesVM = specifyDatesVM;
    this.viewHandler = viewHandler;

    startDate.setValue(specifyDatesVM.getStartDate());
    endDate.setValue(specifyDatesVM.getEndDate());
    errMsg.textProperty().bind(specifyDatesVM.getErrorMsgProperty());
    // Disable submit button if there's an error
    proceedButton.disableProperty()
        .bind(specifyDatesVM.isValidProperty().not());

    // Live validation as the user changes the dates
    startDate.valueProperty().addListener((obs, oldVal, newVal) -> {
      specifyDatesVM.setStartDate(newVal);
    });

    endDate.valueProperty().addListener((obs, oldVal, newVal) -> {
      specifyDatesVM.setEndDate(newVal);
    });
  }

  /**
   * Called when the user clicks the "Proceed" button.
   * Validates the dates and proceeds to the Property List view.
   */
  public void onProceedButtonClicked()
  {
    specifyDatesVM.setStartDate(startDate.getValue());
    specifyDatesVM.setEndDate(endDate.getValue());
    viewHandler.setDates(Date.valueOf(startDate.getValue()),
        Date.valueOf(endDate.getValue()));
    viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
  }

  /**
   * Called when the user clicks the "Back" button.
   * Navigates back to the User Dashboard view.
   */
  public void onBackButtonClicked()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }
}
