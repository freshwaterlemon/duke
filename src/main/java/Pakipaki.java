import java.util.Scanner;
import ui.Ui;
import validation.CommandValidator;
import storage.Storage;
import parser.Parser;

import tasklist.TaskList;

public class Pakipaki {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILEPATH);
    private static final TaskList taskList = new TaskList(storage, ui);

    // // check if details after valid command is missing
    // private static void validateCommandDetails(String command, String taskString)
    // throws PakipakiException {
    // if (taskString.isEmpty()) {
    // throw new PakipakiException("Details after '" + command + "' cannot be
    // empty.");
    // }
    // }

    // get user input and display them
    public static void processUserCommands(Scanner in) {

        // storage.handleStorage(taskList); // load tasklist from storage if availble

        // loop for user input
        while (true) {
            try {

                System.out.println();
                String userInput = in.nextLine().trim();

                // if (userInput.isEmpty()) {
                // System.out.println(("Oops! You didn't type anything. Go ahead, give me a
                // task!\n").indent(4));
                // continue;
                // }
                if (CommandValidator.checkAndPrintIfEmpty(userInput)) {
                    continue; // skip processing this empty input, prompt again
                }
                // get the first word in the sentance
                String command = Parser.getCommand(userInput);
                // get everything after first word if there are more after first word
                String taskString = Parser.getArguments(userInput);

                switch (command) {
                    case "list":
                        // ui.printTaskList(taskList);
                        taskList.listTasks();
                        break;

                    case "bye":
                        ui.endMsg();
                        return;

                    case "mark":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleMark(taskString);
                        taskList.markTask(taskString);
                        break;

                    case "unmark":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleUnmark(taskString);
                        taskList.unmarkTask(taskString);
                        break;

                    case "todo":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleToDo(taskString);
                        taskList.addTodo(taskString);
                        break;

                    case "deadline":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleDeadline(taskString);
                        taskList.addDeadline(taskString);
                        break;

                    case "event":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleEvent(taskString);
                        taskList.addEvent(taskString);
                        break;

                    case "delete":
                        // validateCommandDetails(command, taskString);
                        CommandValidator.validateCommandDetails(command, taskString);
                        // handleDelete(taskString);
                        taskList.deleteTask(taskString);
                        break;

                    default:
                        String unknownCommandMsg = String.format(
                                "Sorry, I do not quite get that.\n\n%s", ui.getUserGuideMsg());
                        throw new PakipakiException(unknownCommandMsg);
                }
            } catch (PakipakiException e) {
                System.out.println((e.getMessage()).indent(4));
            } catch (Exception e) {
                System.out.println(("Something went wrong!: " + e.getMessage()).indent(4));
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) { // close scanner
            ui.startMsg();
            taskList.loadTasks();
            processUserCommands(in);
        }
    }
}