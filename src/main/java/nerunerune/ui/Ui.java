package ui;

import java.util.ArrayList;
import task.Task;

/**
 * Handles user interface display functions such as printing messages,
 * task lists, start/end greetings, and user commands guide.
 */
public class Ui {
    private static final String BOTNAME = "Nerunerune";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String USERGUIDEMSG = """
            List of Task Commands Available:

                1. Type 'command' to see all command available. 
                2. Type 'list' to see all your tasks.
                3. Use 'todo <task>' to add a simple task.
                4. Use 'deadline <task> /by <date/time>' to add a task with a deadline.
                    - Date/time format: DD-MM-YYYY HHMM (e.g. 01-01-2025 1800)
                5. Use 'event <task> /from <start date/time> /to <end date/time>' to add an event.
                    - Date/time format: DD-MM-YYYY HHMM (e.g. 01-01-2025 1800)
                6. Use 'mark <task>' or 'mark' <task number> to mark a task as done.
                7. Use 'unmark <task>' or 'unmark' <task number> to unmark a task as not done.
                8. Use 'delete' <task> or <task number> to delete a task from list and storage.
                9. Type 'bye' to exit the chat.
            """;

    /**
     * Prints a horizontal line for visual separation.
     */
    private void printHorzLine() {
        printMessage(HORIZONTAL_LINE);
    }

    /**
     * Prints the starting greeting message with bot name and command hint.
     */
    public void startMsg() {
        printHorzLine();
        printMessage(
                String.format("Hello! I'm %s, here to help you manage your tasks.", BOTNAME));
        printMessage("Type \"command\" to see all availble command");
        printHorzLine();
    }

    /**
     * Prints the farewell message when the program ends.
     */
    public void endMsg() {
        printMessage(("Until next time!").indent(4));
        printHorzLine();
    }

    /**
     * Prints all tasks in the given task list, formatted with index and indentation.
     * If the list is empty, prints an appropriate message.
     *
     * @param taskList the list of tasks to display
     */
    public void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            printMessage(("Your task list is empty!").indent(4));
        } else {
            printMessage(
                    ("Here's what's on your task list so far: " + "(" + taskList.size() + " in total)").indent(4));
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((((i + 1) + ". " + taskList.get(i)).indent(8)));
            }
        }
    }

    /**
     * Returns the user guide message listing commands available.
     *
     * @return the user guide message string
     */
    public String getUserGuideMsg() {
        return USERGUIDEMSG;
    }

    /**
     * Prints a specified message to the standard output.
     *
     * @param message the message string to print
     */
    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints a blank line to the standard output for spacing.
     */
    public void printLine() {
        System.out.println();
    }
}
