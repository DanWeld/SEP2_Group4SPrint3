import java.sql.*;

/**
 * This utility class fixes issues with the User table by:
 * 1. Adding the missing 'isadmin' column if it doesn't exist
 * 2. Checking for null values in NOT NULL columns
 * 3. Providing detailed information about the database structure
 */
public class FixUserTableNullIssue {
    public static void main(String[] args) {
        try {
            System.out.println("Starting database fix utility...");
            
            // Load the PostgreSQL JDBC driver from the additionalFiles folder
            String driverPath = "additionalFiles/postgresql-42.7.5.jar";
            System.out.println("Loading PostgreSQL JDBC driver from: " + driverPath);
            
            // Register the PostgreSQL driver
            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("PostgreSQL JDBC Driver loaded successfully");
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to load PostgreSQL JDBC driver. Please make sure the driver is in the classpath.");
                System.err.println("Error: " + e.getMessage());
                return;
            }
            
            // Create a connection to the database
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=summerhouse_rental_system",
                "postgres", "viaviavia");
            
            System.out.println("Connected to the database successfully.");
            
            // Check if the schema exists
            checkAndCreateSchema(connection);
            
            // Check if the user table exists and has the correct structure
            checkAndFixUserTable(connection);
            
            // Display all tables in the schema
            listAllTables(connection);
            
            // Close the connection
            connection.close();
            System.out.println("\nDatabase connection closed. Schema update complete.");
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkAndCreateSchema(Connection connection) throws SQLException {
        System.out.println("\nChecking if schema exists...");
        
        // Check if summerhouse_rental_system schema exists
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet schemas = metadata.getSchemas();
        boolean schemaExists = false;
        
        while (schemas.next()) {
            String schemaName = schemas.getString("TABLE_SCHEM");
            if ("summerhouse_rental_system".equalsIgnoreCase(schemaName)) {
                schemaExists = true;
                break;
            }
        }
        
        if (!schemaExists) {
            System.out.println("Schema 'summerhouse_rental_system' does not exist. Creating it now...");
            Statement createSchemaStatement = connection.createStatement();
            createSchemaStatement.execute("CREATE SCHEMA summerhouse_rental_system");
            System.out.println("Schema created successfully.");
        } else {
            System.out.println("Schema 'summerhouse_rental_system' already exists.");
        }
    }
    
    private static void checkAndFixUserTable(Connection connection) throws SQLException {
        System.out.println("\nChecking user table structure...");
        
        // Check if the user table exists
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tables = metadata.getTables(null, "summerhouse_rental_system", "user", null);
        
        if (!tables.next()) {
            System.out.println("The 'user' table does not exist. Creating it now...");
            
            // Create the user table with the correct structure
            Statement createTableStatement = connection.createStatement();
            String createTableSQL = 
                "CREATE TABLE summerhouse_rental_system.\"user\" (" +
                "username VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL, " +
                "isadmin BOOLEAN NOT NULL DEFAULT false)";
            createTableStatement.executeUpdate(createTableSQL);
            
            System.out.println("Table 'user' created successfully with the correct structure.");
        } else {
            System.out.println("The 'user' table exists. Checking its structure...");
            
            // Get all columns in the user table
            ResultSet columns = metadata.getColumns(null, "summerhouse_rental_system", "user", null);
            boolean hasIsAdminColumn = false;
            
            System.out.println("\nCurrent columns in 'user' table:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String nullable = columns.getString("IS_NULLABLE");
                
                System.out.println(columnName + " (" + dataType + ", Nullable: " + nullable + ")");
                
                if ("isadmin".equalsIgnoreCase(columnName)) {
                    hasIsAdminColumn = true;
                }
            }
            
            // Add the isadmin column if it doesn't exist
            if (!hasIsAdminColumn) {
                System.out.println("\nThe 'isadmin' column does not exist. Adding it now...");
                
                Statement alterTableStatement = connection.createStatement();
                String alterTableSQL = "ALTER TABLE summerhouse_rental_system.\"user\" ADD COLUMN isadmin BOOLEAN NOT NULL DEFAULT false";
                alterTableStatement.executeUpdate(alterTableSQL);
                
                System.out.println("Column 'isadmin' added successfully.");
            } else {
                System.out.println("\nThe 'isadmin' column already exists.");
            }
            
            // Check for any NULL values in NOT NULL columns
            System.out.println("\nChecking for NULL values in NOT NULL columns...");
            
            Statement checkNullStatement = connection.createStatement();
            ResultSet nullCheckResult = checkNullStatement.executeQuery(
                "SELECT * FROM summerhouse_rental_system.\"user\" WHERE " +
                "username IS NULL OR " +
                "email IS NULL OR " +
                "password IS NULL OR " +
                "isadmin IS NULL");
            
            if (nullCheckResult.next()) {
                System.out.println("WARNING: Found rows with NULL values in NOT NULL columns!");
                
                // Display the problematic rows
                do {
                    String username = nullCheckResult.getString("username");
                    String email = nullCheckResult.getString("email");
                    String password = nullCheckResult.getString("password");
                    Boolean isAdmin = null;
                    try {
                        isAdmin = nullCheckResult.getBoolean("isadmin");
                        if (nullCheckResult.wasNull()) {
                            isAdmin = null;
                        }
                    } catch (SQLException e) {
                        // Column might not exist
                        isAdmin = null;
                    }
                    
                    System.out.println("Problematic row: username=" + username + 
                                     ", email=" + email + 
                                     ", password=" + password + 
                                     ", isadmin=" + isAdmin);
                } while (nullCheckResult.next());
                
                // Fix NULL values in isadmin column
                System.out.println("\nFixing NULL values in the isadmin column...");
                Statement fixNullStatement = connection.createStatement();
                int rowsUpdated = fixNullStatement.executeUpdate(
                    "UPDATE summerhouse_rental_system.\"user\" SET isadmin = false WHERE isadmin IS NULL");
                
                System.out.println("Fixed " + rowsUpdated + " rows with NULL values in isadmin column.");
            } else {
                System.out.println("No NULL values found in NOT NULL columns. Data integrity is good!");
            }
        }
        
        // Show table data
        displayUserTableData(connection);
    }
    
    private static void displayUserTableData(Connection connection) throws SQLException {
        System.out.println("\nCurrent data in 'user' table:");
        
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM summerhouse_rental_system.\"user\"");
        
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        
        // Print column headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rsMetaData.getColumnName(i) + "\t");
        }
        System.out.println();
        
        // Print separator
        for (int i = 1; i <= columnCount; i++) {
            System.out.print("--------\t");
        }
        System.out.println();
        
        // Print data rows
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
        
        if (rowCount == 0) {
            System.out.println("No data found in the user table.");
        } else {
            System.out.println("Total rows: " + rowCount);
        }
    }
    
    private static void listAllTables(Connection connection) throws SQLException {
        System.out.println("\nListing all tables in summerhouse_rental_system schema:");
        
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tables = metadata.getTables(null, "summerhouse_rental_system", null, new String[]{"TABLE"});
        
        int tableCount = 0;
        while (tables.next()) {
            tableCount++;
            String tableName = tables.getString("TABLE_NAME");
            System.out.println(tableCount + ". " + tableName);
        }
        
        if (tableCount == 0) {
            System.out.println("No tables found in the schema.");
        }
    }
}
