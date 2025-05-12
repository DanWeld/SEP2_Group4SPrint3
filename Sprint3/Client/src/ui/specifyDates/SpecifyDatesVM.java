package ui.specifyDates;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

public class SpecifyDatesVM
{
  private ObjectProperty<Date> startDate;
  private ObjectProperty<Date> endDate;
  private StringProperty errorMsg;
  private BooleanProperty valid;

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

  public BooleanProperty isValidProperty()
  {
    return valid;
  }

  public LocalDate getStartDate()
  {
    return startDate.get().toLocalDate();
  }

  public LocalDate getEndDate()
  {
    return endDate.get().toLocalDate();
  }

  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }

  public void setStartDate(LocalDate value)
  {
    startDate.set(Date.valueOf(value));
    validateDates();
  }

  public void setEndDate(LocalDate value)
  {
    endDate.set(Date.valueOf(value));
    validateDates();
  }

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
