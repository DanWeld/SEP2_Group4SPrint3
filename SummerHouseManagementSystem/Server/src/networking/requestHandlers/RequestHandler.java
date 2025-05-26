package networking.requestHandlers;

import java.io.PrintWriter;

/**
 * Interface for handling requests in the networking layer.
 * Implementations should define how to handle specific actions
 * based on the handler type and action name.
 */
public interface RequestHandler
{
  boolean canHandle(String handler, String action);
  void handle(String action, String payload, PrintWriter out);
}
