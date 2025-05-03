package ui.specifyDates;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

import java.sql.Date;

public class SpecifyDatesController
{
  private @FXML DatePicker startDate;
  private @FXML DatePicker endDate;
  private @FXML Label errMsg;
  private @FXML Button proceedButton;

  private SpecifyDatesVM specifyDatesVM;
  private ViewHandler viewHandler;

  public SpecifyDatesController()
  {
  }

  public void initialize(SpecifyDatesVM specifyDatesVM, ViewHandler viewHandler)
  {
    this.specifyDatesVM = specifyDatesVM;
    this.viewHandler = viewHandler;

    startDate.setValue(specifyDatesVM.getStartDate());
    endDate.setValue(specifyDatesVM.getEndDate());
    errMsg.textProperty().bind(specifyDatesVM.getErrorMsgProperty());
    // Disable submit button if there's an error
    proceedButton.disableProperty().bind(specifyDatesVM.isValidProperty().not());

    // Live validation as the user changes the dates
    startDate.valueProperty().addListener((obs, oldVal, newVal) -> {
      specifyDatesVM.setStartDate(newVal);
    });

    endDate.valueProperty().addListener((obs, oldVal, newVal) -> {
      specifyDatesVM.setEndDate(newVal);
    });
  }

  public void onProceedButtonClicked()
  {
    specifyDatesVM.setStartDate(startDate.getValue());
    specifyDatesVM.setEndDate(endDate.getValue());
    viewHandler.setDates(Date.valueOf(startDate.getValue()), Date.valueOf(endDate.getValue()));
    viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
  }
}
