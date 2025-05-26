package utilities.logging;

/**
 * Logger interface for logging messages with different log levels.
 * Implementations of this interface can define how and where to log messages.
 */
public class ConsoleLogger implements Logger
{
    private LogLevel logAboveThisLevel;

    /**
     * Constructs a ConsoleLogger that logs messages above the specified log level.
     *
     * @param logAboveThisLevel The minimum log level for messages to be logged.
     */
    public ConsoleLogger(LogLevel logAboveThisLevel)
    {
        this.logAboveThisLevel = logAboveThisLevel;
    }

    /**
     * Logs a message to the console if its log level is above or equal to the specified level.
     *
     * @param text The message to log.
     * @param level The log level of the message.
     */
    @Override
    public void log(String text, LogLevel level)
    {
        if (level.ordinal() >= logAboveThisLevel.ordinal())
        {
            System.out.println(level.name() + ": " + text);
        }
    }
}
