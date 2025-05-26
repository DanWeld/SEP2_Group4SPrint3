package utilities.logging;

/**
 * Logger interface for logging messages with different log levels.
 * Implementations of this interface can define how and where to log messages.
 */
public interface Logger
{
    void log(String text, LogLevel level);
}
