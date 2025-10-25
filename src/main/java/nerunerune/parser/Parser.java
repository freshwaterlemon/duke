package nerunerune.parser;

import nerunerune.ui.Ui;
import nerunerune.validation.CommandValidator;
import nerunerune.task.Task;
import nerunerune.task.Todo;
import nerunerune.task.Deadline;
import nerunerune.task.Event;
import java.io.IOException;
import java.time.LocalDateTime;

import nerunerune.command.*;
import nerunerune.exception.NeruneruneException;

/**
 * Provides methods to parse user input into commands or tasks.
 * Handle extraction and validation of command keywords and arguments,
 * and translates them into corresponding Command objects or Task instances.
 */
public class Parser {

    /**
     * Extracts the command keyword from the user's input string.
     *
     * @param userInput the full input string from the user
     * @return the command word in lowercase (e.g. todo, list, mark)
     */
    public static String getCommand(String userInput) {
        return userInput.split(" ")[0].toLowerCase();
    }

    /**
     * Extracts the argument portion of the user's input string after the command word.
     *
     * @param userInput the full input string from the user
     * @return the trimmed argument string, or empty string if none
     */
    public static String getArguments(String userInput) {
        String command = getCommand(userInput);
        return userInput.length() > command.length() ? userInput.substring(command.length()).trim() : "";
    }

    /**
     * Parses a todo task description into a Todo object.
     *
     * @param description the description of the todo task
     * @return new Todo task instance
     */
    public static Todo parseTodo(String description) {
        return new Todo(description);
    }

    /**
     * Parses a deadline task description into a Deadline object.
     *
     * @param description the full deadline description including "/by" delimiter and timing
     * @return new Deadline task instance
     * @throws IOException if the description does not contain "/by"
     * @throws NeruneruneException if date-time parsing fails
     */
    public static Deadline parseDeadline(String description) throws IOException, NeruneruneException {
        int findIndex = description.indexOf("/by");
        if (findIndex == -1) {
            throw new IOException("Deadline must contain '/by'");
        }
        String deadlineDescription = description.substring(0, findIndex).trim();
        String deadlineTiming = description.substring(findIndex + 3).trim();

        // create dates from strings
        LocalDateTime byTiming = DateTimeParser.parseDateTime(deadlineTiming);
        return new Deadline(deadlineDescription, byTiming);
    }

    /**
     * Parses an event task description into an Event object.
     *
     * @param description the full event description including "/from" and "/to" delimiters with timings
     * @return new Event task instance
     * @throws IOException if delimiters are missing or in wrong order
     * @throws NeruneruneException if date-time parsing fails
     */
    public static Event parseEvent(String description) throws IOException, NeruneruneException {
        int findIndexFrom = description.indexOf("/from");
        int findIndexTo = description.indexOf("/to");
        if (findIndexFrom == -1 || findIndexTo == -1 || findIndexFrom > findIndexTo) {
            throw new IOException("Event must contain '/from' and '/to' in correct order");
        }
        String eventDescription = description.substring(0, findIndexFrom).trim();
        String eventTimingFrom = description.substring(findIndexFrom + 5, findIndexTo).trim();
        String eventTimingTo = description.substring(findIndexTo + 3).trim();

        // create dates from strings
        LocalDateTime fromTiming = DateTimeParser.parseDateTime(eventTimingFrom);
        LocalDateTime toTiming = DateTimeParser.parseDateTime(eventTimingTo);
        return new Event(eventDescription, fromTiming, toTiming);
    }

    /**
     * Parses a stored task line string into a corresponding Task object.
     * Split stored task line string using '|'
     *
     * @param line the stored task line string, formatted with delimiters
     * @return Task object corresponding to the stored line
     * @throws IOException if the line is corrupted or task type unknown
     * @throws NeruneruneException if stored date-time parsing fails
     */
    public static Task parseTaskLine(String line) throws IOException, NeruneruneException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IOException("Corrupted line: " + line);
        }
        String taskType = parts[0];
        boolean isDone = parts[1].equals("1"); // true if 1 -> mark with X
        String description = parts[2];

        switch (taskType) {
            case "T": // todo
                if (parts.length > 3)
                    throw new IOException("Corrupted todo line: " + line);
                return new Todo(description, isDone);
            case "D": // deadline
                if (parts.length < 4)
                    throw new IOException("Corrupted deadline line: " + line);
                LocalDateTime deadlineBy = DateTimeParser.parseStorageDateTime(parts[3]);
                return new Deadline(description, deadlineBy, isDone);
            case "E": // event
                if (parts.length < 5)
                    throw new IOException("Corrupted event line: " + line);
                LocalDateTime eventFrom = DateTimeParser.parseStorageDateTime(parts[3]);
                LocalDateTime eventTo = DateTimeParser.parseStorageDateTime(parts[4]);
                return new Event(description, eventFrom, eventTo, isDone);
            default:
                throw new IOException("Unknown task type in line: " + line);
        }
    }

    /**
     * Parses the user input into a Command object, validating input and arguments.
     *
     * @param userInput the full input string from the user
     * @param ui the user interface instance for interaction or validation context
     * @return the Command object representing the user's requested action
     * @throws Exception for unknown commands or invalid input formats
     */
    public static Command parseCommand(String userInput, Ui ui) throws Exception {
        CommandValidator.validateUserInputNotEmpty(userInput);

        String ExtractedCommand = getCommand(userInput);
        String taskString = getArguments(userInput);

        switch (ExtractedCommand) {
            case "list":
                return new PrintTaskListCommand();
            case "bye":
                return new ExitCommand();
            case "command":
                return new ViewAllCommand();
            case "mark":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new MarkCommand(taskString);
            case "unmark":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new UnmarkCommand(taskString);
            case "todo":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new AddTodoCommand(taskString);
            case "deadline":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new AddDeadlineCommand(taskString);
            case "event":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new AddEventCommand(taskString);
            case "delete":
                CommandValidator.validateCommandDetails(ExtractedCommand, taskString);
                return new DeleteCommand(taskString);
            default:
                String unknownCommandMsg = "Sorry, I do not quite get that.\nType \"command\" to see all available command";
                throw new NeruneruneException(unknownCommandMsg);
        }
    }
}
