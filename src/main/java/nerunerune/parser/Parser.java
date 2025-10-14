package parser;

import ui.Ui;
import validation.CommandValidator;
import task.Task;
import task.Todo;
import task.Deadline;
import task.Event;
import java.io.IOException;

import command.PrintTaskListCommand;
import command.ExitCommand;
import command.MarkCommand;
import command.UnmarkCommand;
import command.AddTodoCommand;
import command.Command;
import command.AddDeadlineCommand;
import command.AddEventCommand;
import command.DeleteCommand;
import command.ViewAllCommand;
import exception.NeruneruneException;

public class Parser {

    public static String getCommand(String userInput) {
        return userInput.split(" ")[0].toLowerCase();
    }

    public static String getArguments(String userInput) {
        String command = getCommand(userInput);
        return userInput.length() > command.length() ? userInput.substring(command.length()).trim() : "";
    }

    public static Task parseTodo(String description) {
        return new Todo(description);
    }

    public static Task parseDeadline(String description) throws IOException {
        int findIndex = description.indexOf("/by");
        if (findIndex == -1) {
            throw new IOException("Deadline must contain '/by'");
        }
        String deadlineDescription = description.substring(0, findIndex).trim();
        String deadlineTiming = description.substring(findIndex + 3).trim();
        return new Deadline(deadlineDescription, deadlineTiming);
    }

    public static Task parseEvent(String description) throws IOException {
        int findIndexFrom = description.indexOf("/from");
        int findIndexTo = description.indexOf("/to");
        if (findIndexFrom == -1 || findIndexTo == -1 || findIndexFrom > findIndexTo) {
            throw new IOException("Event must contain '/from' and '/to' in correct order");
        }
        String eventDescription = description.substring(0, findIndexFrom).trim();
        String eventTimingFrom = description.substring(findIndexFrom + 5, findIndexTo).trim();
        String eventTimingTo = description.substring(findIndexTo + 3).trim();
        return new Event(eventDescription, eventTimingFrom, eventTimingTo);
    }

    public static Task parseTaskLine(String line) throws IOException {
        String[] parts = line.split(" \\| "); // split by '|'
        if (parts.length < 3) { // check
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
                return new Deadline(description, parts[3], isDone);
            case "E": // event
                if (parts.length < 5)
                    throw new IOException("Corrupted event line: " + line);
                return new Event(description, parts[3], parts[4], isDone);
            default:
                throw new IOException("Unknown task type in line: " + line);
        }
    }

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
                String unknownCommandMsg = String.format(
                        "Sorry, I do not quite get that.\nType \"command\" to see all availble command");
                throw new NeruneruneException(unknownCommandMsg);
        }
    }
}
