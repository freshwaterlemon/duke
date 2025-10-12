package command;

import java.util.Scanner;

import parser.Parser;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;
import validation.CommandValidator;

public class CommandHandler {
    private final Ui ui;
    private final TaskList taskList;
    private final Storage storage;

    public CommandHandler(Ui ui, TaskList taskList, Storage storage) {
        this.ui = ui;
        this.taskList = taskList;
        this.storage = storage;
    }

    // get user input and display them
    public void processUserCommands(Scanner in) {

        // loop for user input
        while (true) {
            try {

                System.out.println();
                String userInput = in.nextLine().trim();

                if (CommandValidator.checkAndPrintIfEmpty(userInput)) {
                    continue; // skip processing this empty input, prompt again
                }
                // get the first word in the sentance
                String command = Parser.getCommand(userInput);
                // get everything after first word if there are more after first word
                String taskString = Parser.getArguments(userInput);

                switch (command) {
                    case "list":
                        taskList.listTasks();
                        break;

                    case "bye":
                        ui.endMsg();
                        return;

                    case "mark":
                    case "unmark":
                    case "todo":
                    case "deadline":
                    case "event":
                    case "delete":
                        CommandValidator.validateCommandDetails(command, taskString);
                        processTaskCommand(command, taskString);
                        storage.saveTasksToStorage(taskList.getTaskList()); // add and save new tasklist to tasks.txt
                        break;

                    default:
                        String unknownCommandMsg = String.format(
                                "Sorry, I do not quite get that.\n\n%s", ui.getUserGuideMsg());
                        // throw new PakipakiException(unknownCommandMsg);
                        throw new Exception(unknownCommandMsg);
                }
                // } catch (PakipakiException e) {
                // System.out.println((e.getMessage()).indent(4));
            } catch (Exception e) {
                System.out.println(("Something went wrong!: " + e.getMessage()).indent(4));
            }
        }
    }

    private void processTaskCommand(String command, String taskString) throws Exception {
        switch (command) {
            case "mark":
                taskList.markTask(taskString);
                break;
            case "unmark":
                taskList.unmarkTask(taskString);
                break;
            case "todo":
                taskList.addTodo(taskString);
                break;
            case "deadline":
                taskList.addDeadline(taskString);
                break;
            case "event":
                taskList.addEvent(taskString);
                break;
            case "delete":
                taskList.deleteTask(taskString);
                break;
        }
    }
}
