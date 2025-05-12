package ui.adminPropertyList;

import dtos.Facilities;
import dtos.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Admin Property List view
 */
public class AdminPropertyListCtrl {
    @FXML private TableView<Property> propertyTable;
    @FXML private TableColumn<Property, Integer> idColumn;
    @FXML private TableColumn<Property, String> nameColumn;
    @FXML private TableColumn<Property, String> addressColumn;
    @FXML private TableColumn<Property, Integer> bedroomsColumn;
    @FXML private TableColumn<Property, Integer> bathroomsColumn;
    @FXML private TableColumn<Property, Double> priceColumn;
    
    @FXML private TextField propertyNameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField addressField;
    @FXML private Spinner<Integer> bedroomsSpinner;
    @FXML private Spinner<Integer> bathroomsSpinner;
    @FXML private TextField priceField;
    
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button viewHistoryButton;
    @FXML private Label messageLabel;
    
    private AdminPropertyListVM viewModel;
    private ViewHandler viewHandler;
    
    public AdminPropertyListCtrl() {
        // Empty constructor
    }
    
    public void initialize(AdminPropertyListVM vm, ViewHandler vh) {
        this.viewModel = vm;
        this.viewHandler = vh;
          // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        bedroomsColumn.setCellValueFactory(new PropertyValueFactory<>("bedrooms"));
        bathroomsColumn.setCellValueFactory(new PropertyValueFactory<>("bathrooms"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Bind table items
        propertyTable.setItems(viewModel.getProperties());
        
        // Configure spinners
        bedroomsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        bathroomsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        
        // Set up selection listener to populate form fields when a property is selected
        propertyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewModel.setSelectedProperty(newSelection);
                propertyNameField.setText(newSelection.getName());
                descriptionArea.setText(newSelection.getDescription());
                addressField.setText(newSelection.getAddress());
                bedroomsSpinner.getValueFactory().setValue(newSelection.getBedrooms());
                bathroomsSpinner.getValueFactory().setValue(newSelection.getBathrooms());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                
                // Enable buttons for selected property
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                viewHistoryButton.setDisable(false);
            } else {
                clearForm();
                
                // Disable buttons when no property is selected
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                viewHistoryButton.setDisable(true);
            }
        });
        
        // Initially disable buttons that require selection
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        viewHistoryButton.setDisable(true);
        
        // Bind message label
        messageLabel.textProperty().bind(viewModel.messageProperty());
        
        // Load properties
        viewModel.loadProperties();
    }
    
    private void clearForm() {
        propertyNameField.clear();
        descriptionArea.clear();
        addressField.clear();
        bedroomsSpinner.getValueFactory().setValue(1);
        bathroomsSpinner.getValueFactory().setValue(1);
        priceField.clear();
        viewModel.setSelectedProperty(null);
    }
      @FXML
    public void onAddProperty() {
        try {
            String name = propertyNameField.getText();
            String description = descriptionArea.getText();
            String address = addressField.getText();
            int bedrooms = bedroomsSpinner.getValue();
            int bathrooms = bathroomsSpinner.getValue();
            double price = Double.parseDouble(priceField.getText());
            
            // For simplicity, generate a random ID for new property (in real app, server would assign ID)
            int id = (int)(Math.random() * 1000);
            
            // Create default facilities
            Facilities facilities = new Facilities(true, true, false, false, false);
            
            // Create new property with available constructor
            Property newProperty = new Property(id, name, price, true, facilities);
            
            boolean success = viewModel.addProperty(newProperty);
            
            if (success) {
                clearForm();
                propertyTable.refresh();
            }
        } catch (NumberFormatException e) {
            viewModel.messageProperty().set("Invalid price format");
        }
    }
      @FXML
    public void onUpdateProperty() {
        Property selectedProperty = viewModel.getSelectedProperty();
        if (selectedProperty != null) {
            try {
                // Only update the properties that the Property class supports
                selectedProperty.setName(propertyNameField.getText());
                selectedProperty.setDescription(descriptionArea.getText()); // Will be ignored in current model
                selectedProperty.setAddress(addressField.getText());        // Will be ignored in current model
                selectedProperty.setBedrooms(bedroomsSpinner.getValue());   // Will be ignored in current model
                selectedProperty.setBathrooms(bathroomsSpinner.getValue()); // Will be ignored in current model
                selectedProperty.setPrice(Double.parseDouble(priceField.getText()));
                
                boolean success = viewModel.updateProperty(selectedProperty);
                if (success) {
                    propertyTable.refresh();
                }
            } catch (NumberFormatException e) {
                viewModel.messageProperty().set("Invalid price format");
            }
        }
    }
    
    @FXML
    public void onDeleteProperty() {
        Property selectedProperty = viewModel.getSelectedProperty();
        if (selectedProperty != null) {
            boolean success = viewModel.deleteProperty(selectedProperty);
            if (success) {
                clearForm();
                propertyTable.refresh();
            }
        }
    }
    
  @FXML
    public void onViewHistory() {
        Property selectedProperty = viewModel.getSelectedProperty();
        if (selectedProperty != null) {
            // Navigate to property history view
            viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_HISTORY);
        }
    }
    
    @FXML
    public void onBack() {
        viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
    }
}
