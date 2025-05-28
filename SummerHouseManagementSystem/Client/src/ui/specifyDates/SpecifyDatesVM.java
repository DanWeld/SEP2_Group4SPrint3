package ui.specifyDates;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

/**
 * ViewModel for the SpecifyDates view.
 * This class handles the logic for specifying start and end dates.
 *
 * @author Group 4
 * @version 1.0
 */
public class SpecifyDatesVM
{
  private ObjectProperty<Date> startDate;
  private ObjectProperty<Date> endDate;
  private StringProperty errorMsg;
  private BooleanProperty valid;

  /**
   * Constructor for SpecifyDatesVM.
   * Initializes the start and end dates to default values and sets up properties.
   */
  public SpecifyDatesVM()
  {
    startDate = new SimpleObjectProperty<>();
    endDate = new SimpleObjectProperty<>();
    errorMsg = new SimpleStringProperty();
    valid = new SimpleBooleanProperty(true);

    startDate.set(Date.valueOf(LocalDate.now().plusDays(7)));
    endDate.set(Date.valueOf(LocalDate.now().plusDays(14)));
    errorMsg.setValue("");
  }

  /**
   * Boolean property indicating whether the specified dates are valid.
   * This property is true if the dates are valid, false otherwise.
   *
   * @return BooleanProperty indicating validity of dates
   */
  public BooleanProperty isValidProperty()
  {
    return valid;
  }

  /**
   * Gets the start date as a LocalDate.
   *
   * @return LocalDate representing the start date
   */
  public LocalDate getStartDate()
  {
    return startDate.get().toLocalDate();
  }

  /**
   * Gets the end date as a LocalDate.
   *
   * @return LocalDate representing the end date
   */
  public LocalDate getEndDate()
  {
    return endDate.get().toLocalDate();
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   *
   * @return the property values as JavaFX properties
   */
  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }

  /**
   * Sets the start date to the specified LocalDate.
   * Validates the dates after setting.
   *
   * @param value LocalDate to set as start date
   */
  public void setStartDate(LocalDate value)
  {
    startDate.set(Date.valueOf(value));
    validateDates();
  }

  /**
   * Sets the end date to the specified LocalDate.
   * Validates the dates after setting.
   *
   * @param value LocalDate to set as end date
   */
  public void setEndDate(LocalDate value)
  {
    endDate.set(Date.valueOf(value));
    validateDates();
  }

  /**
   * Validates the start and end dates.
   * Checks if both dates are selected, if the start date is not before today,
   * and if the end date is not before the start date.
   * Updates the error message and validity property accordingly.
   */
  private void validateDates()
  {
    LocalDate now = LocalDate.now();
    LocalDate start =
        startDate.get() != null ? startDate.get().toLocalDate() : null;
    LocalDate end = endDate.get() != null ? endDate.get().toLocalDate() : null;

    if (start == null || end == null)
    {
      errorMsg.set("Both dates must be selected");
      valid.set(false);
    }
    else if (start.isBefore(now))
    {
      errorMsg.set("Start date cannot be before today");
      valid.set(false);
    }
    else if (end.isBefore(start))
    {
      errorMsg.set("End date cannot be before start date");
      valid.set(false);
    }
    else
    {
      errorMsg.set("");
      valid.set(true);
    }
  }
}
