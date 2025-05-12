import java.sql.*;

/**
 * This utility class fixes the User table schema by adding the missing 'isadmin' column.
 * Run this class to resolve the "ERROR: column 'isadmin' of relation 'user' does not exist" error.
 */
public class FixUserTableSchema {
    public static void main(String[] args) {
        try {
            // Register the PostgreSQL driver
            DriverManager.registerDriver(new org.postgresql.Driver());
            
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=summerhouse_rental_system",
                "postgres", "viaviavia");
            
            System.out.println("Connected to the database successfully.");
            
            // Check if the column exists
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, "summerhouse_rental_system", "user", "isadmin");
            
            if (!resultSet.next()) {
                System.out.println("The 'isadmin' column does not exist in the 'user' table. Adding it now...");
                
                // Create a statement
                Statement statement = connection.createStatement();
                
                // Add the column
                String sql = "ALTER TABLE summerhouse_rental_system.\"user\" ADD COLUMN isadmin BOOLEAN NOT NULL DEFAULT false";
                statement.executeUpdate(sql);
                
                System.out.println("Column 'isadmin' has been added successfully to the 'user' table.");
            } else {
                System.out.println("The 'isadmin' column already exists in the 'user' table. No changes needed.");
            }
            
            // Now print the user table structure to verify
            System.out.println("\nCurrent 'user' table structure:");
            Statement statement = connection.createStatement();
            ResultSet tableInfo = statement.executeQuery(
                "SELECT column_name, data_type FROM information_schema.columns " +
                "WHERE table_schema = 'summerhouse_rental_system' AND table_name = 'user'");
            
            while (tableInfo.next()) {
                System.out.println(tableInfo.getString("column_name") + ": " + tableInfo.getString("data_type"));
            }
            
            // Close the connection
            connection.close();
            System.out.println("\nDatabase connection closed. Schema update complete.");
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
