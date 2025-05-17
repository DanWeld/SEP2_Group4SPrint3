package utilities.logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileLogger implements Logger
{
    private LogLevel logAboveThisLevel;
    private String filePath;

    public FileLogger(LogLevel logAboveThisLevel, String filePath)
    {
        this.logAboveThisLevel = logAboveThisLevel;
        this.filePath = filePath;
    }
    @Override
    public void log(String text, LogLevel level) {
        if (level.ordinal() >= this.logAboveThisLevel.ordinal())
        {
            if (filePath != null)
            {
                try {
                    PrintWriter writer = new PrintWriter(filePath);
                    writer.println(level.name() + ": " + text);
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + filePath);
                }
            }
            else
            {
                System.err.println("File path is null");
            }
        }
    }
}
