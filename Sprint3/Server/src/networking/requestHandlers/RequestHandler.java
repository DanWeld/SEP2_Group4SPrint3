package networking.requestHandlers;

import java.io.PrintWriter;

public interface RequestHandler
{
  boolean canHandle(String handler, String action);
  void handle(String action, String payload, PrintWriter out);
}
