// file: `Sprint3/Server/src/networking/requestHandlers/PropertyRequestHandlerTestConnection.java`
package networking.requestHandlers;

import dtos.Request;
import dtos.User;
import model.property.PropertyModel;
import model.property.PropertyModelManager;
import services.ServiceProvider;
import utils.JsonParser;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

public class PropertyRequestHandlerTestConnection {
  public static void main(String[] args) {
    // Create a non-admin user
    User normalUser = new User("testUser", "asdasd@asdasd","sdaha@ASd123", false);

    //payload is a list of two dates starting 2025-05-12 and ending 2025-05-15
    List<Date> dates = List.of(Date.valueOf("2025-05-12"), Date.valueOf("2025-05-15"));

    //dates to json
    String payload = JsonParser.toJson(dates);

    // Prepare a request with "readAll" action
    Request request = new Request("property", "readAll", null, normalUser);

    ServiceProvider serviceProvider = new ServiceProvider();

    // Create the PropertyRequestHandler
    RequestHandler handler = serviceProvider.getPropertyRequestHandler(normalUser);

    // Handle the request, sending output to System.out
    PrintWriter out = new PrintWriter(System.out, true);
    handler.handle(request.action(), JsonParser.toJson(request.payload()), out);
  }
}