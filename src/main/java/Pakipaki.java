import java.util.Scanner;

import command.CommandHandler;
import ui.Ui;
import storage.Storage;

import tasklist.TaskList;

public class Pakipaki {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILEPATH);
    private static final TaskList taskList = new TaskList(storage, ui);

    // // get user input and display them
    // public static void processUserCommands(Scanner in) {

    // // loop for user input
    // while (true) {
    // try {

    // System.out.println();
    // String userInput = in.nextLine().trim();

    // if (CommandValidator.checkAndPrintIfEmpty(userInput)) {
    // continue; // skip processing this empty input, prompt again
    // }
    // // get the first word in the sentance
    // String command = Parser.getCommand(userInput);
    // // get everything after first word if there are more after first word
    // String taskString = Parser.getArguments(userInput);

    // switch (command) {
    // case "list":
    // taskList.listTasks();
    // break;

    // case "bye":
    // ui.endMsg();
    // return;

    // case "mark":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.markTask(taskString);
    // break;

    // case "unmark":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.unmarkTask(taskString);
    // break;

    // case "todo":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.addTodo(taskString);
    // break;

    // case "deadline":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.addDeadline(taskString);
    // break;

    // case "event":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.addEvent(taskString);
    // break;

    // case "delete":
    // CommandValidator.validateCommandDetails(command, taskString);
    // taskList.deleteTask(taskString);
    // break;

    // default:
    // String unknownCommandMsg = String.format(
    // "Sorry, I do not quite get that.\n\n%s", ui.getUserGuideMsg());
    // throw new PakipakiException(unknownCommandMsg);
    // }
    // } catch (PakipakiException e) {
    // System.out.println((e.getMessage()).indent(4));
    // } catch (Exception e) {
    // System.out.println(("Something went wrong!: " + e.getMessage()).indent(4));
    // }
    // }
    // }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) { // close scanner
            ui.startMsg();
            taskList.loadTasks();
            // processUserCommands(in);
            CommandHandler processor = new CommandHandler(ui, taskList, storage);
            processor.processUserCommands(in);
        }
    }
}