package nerunerune.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import nerunerune.command.Command;
import nerunerune.exception.NeruneruneException;
import nerunerune.parser.DateTimeParser;
import nerunerune.storage.Storage;
import nerunerune.task.Deadline;
import nerunerune.task.Event;
import nerunerune.task.Task;
import nerunerune.tasklist.TaskList;

/**
 * Handles user interface display functions such as printing messages,
 * task lists, start/end greetings, and user commands guide.
 */
public class Ui {
    private static final String BOT_NAME = "Nerunerune";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String USER_GUIDE_MSG = """
            List of Task Commands Available:
            
                1.  Type 'command' to see all command available.
                2.  Type 'list' to see all your tasks.
                3.  Use 'todo <task>' to add a simple task.
                4.  Use 'deadline <task> /by <date/time>' to add a task with a deadline.
                      - Date/time format: DD-MM-YYYY HHmm (e.g. 01-01-2025 1800)
                5.  Use 'event <task> /from <start date/time> /to <end date/time>' to add an event.
                      - Date/time format: DD-MM-YYYY HHmm (e.g. 01-01-2025 1800)
                6.  Use 'mark <task>' or 'mark' <task number> to mark a task as done.
                      - Use 'mark backdated' to mark all tasks with passed deadlines/events as done.
                7.  Use 'unmark <task>' or 'unmark' <task number> to unmark a task as not done.
                8.  Use 'delete' <task> or <task number> to delete a task from list and storage.
                      - Use 'delete all done' to delete all completed tasks at once.
                9.  Use 'schedule <date>' to view tasks scheduled for a specific date.
                      - Date format: DD-MM-YYYY (e.g. 26-10-2025)
                      - Shortcuts: schedule <today>, <tomorrow>, <yesterday>
                10. Type 'bye' to exit the chat.
            """;

    /**
     * Gets the starting greeting message for GUI display.
     *
     * @return The greeting message as a String
     */
    public static String getStartMsg() {
        return String.format("Hello! I'm %s, here to help you manage your tasks.", BOT_NAME)
                + "\nType \"command\" to see all available command";
    }

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
                String.format("Hello! I'm %s, here to help you manage your tasks.", BOT_NAME));
        printMessage("Type \"command\" to see all available command");
        printHorzLine();
    }

    /**
     * Prints the farewell message when the program ends.
     */
    public void endMsg() {
        printMessage(("Until next time!").indent(4));
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
                printMessage(((i + 1) + ". " + taskList.get(i)).indent(8));
            }
        }
    }

    /**
     * Returns the user guide message listing commands available.
     *
     * @return the user guide message string
     */
    public String getUserGuideMsg() {
        return USER_GUIDE_MSG;
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

    /**
     * Displays the schedule of tasks for a specific date, grouped by task type.
     * Tasks are categorized into "Deadlines" and "Events" sections, with each section
     * numbered independently.
     * Shows all tasks (deadlines and events) that occur on the given date,
     * categorized, numbered and formatted for readability.
     * If no tasks are scheduled, displays an appropriate message.
     *
     * @param tasks the TaskList containing all tasks to check
     * @param date  the date for which to display the schedule
     */
    public void showSchedule(TaskList tasks, LocalDate date) {
        printMessage("Schedule for " + DateTimeParser.formatForSchedule(date) + ":");

        ArrayList<Task> deadlines = new ArrayList<>();
        ArrayList<Task> events = new ArrayList<>();

        filterAndGroupTasks(tasks, date, deadlines, events);
        displayScheduleContent(deadlines, events);

    }

    /**
     * Filters tasks by the specified date and groups them by type.
     * Iterates through all tasks and categorizes those occurring on the given date
     * into separate lists for Deadlines and Events. Todo tasks are excluded as they
     * have no specific date.
     *
     * @param tasks     the TaskList containing all tasks
     * @param date      the date to filter tasks by
     * @param deadlines the list to populate with Deadline tasks (modified by this method)
     * @param events    the list to populate with Event tasks (modified by this method)
     */
    private void filterAndGroupTasks(TaskList tasks, LocalDate date,
                                     ArrayList<Task> deadlines, ArrayList<Task> events) {
        for (Task task : tasks.getTaskList()) {
            if (!task.occursOn(date)) {
                continue;
            }

            if (task instanceof Deadline) {
                deadlines.add(task);
            } else if (task instanceof Event) {
                events.add(task);
            }
        }
    }

    /**
     * Displays the formatted schedule content for the filtered tasks.
     * Shows separate sections for Deadlines and Events, with a total count at the bottom.
     * If both lists are empty, displays a message indicating no tasks are scheduled.
     *
     * @param deadlines the list of Deadline tasks to display
     * @param events    the list of Event tasks to display
     */
    private void displayScheduleContent(ArrayList<Task> deadlines, ArrayList<Task> events) {
        if (deadlines.isEmpty() && events.isEmpty()) {
            printMessage(("No tasks scheduled for this date.").indent(4));
            return;
        }

        displayTaskCategory("Deadlines", deadlines);
        displayTaskCategory("Events", events);
        printMessage("Total: " + (deadlines.size() + events.size()) + " task(s)");
    }

    /**
     * Displays a category of tasks with a header and numbered list.
     * If the task list is empty, the category is not displayed.
     * Each task is numbered starting from 1 and indented for readability.
     *
     * @param categoryName the name of the category (e.g., "Deadlines", "Events")
     * @param tasks        the list of tasks to display in this category
     */
    private void displayTaskCategory(String categoryName, ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }

        printMessage((categoryName + ":").indent(4));
        for (int i = 0; i < tasks.size(); i++) {
            printMessage(((i + 1) + ". " + tasks.get(i)).indent(8));
        }
    }

    /**
     * Displays the list of tasks that match the search criteria.
     * If the list is empty, displays a "no matches found" message.
     * Otherwise, displays each matching task with its index number.
     *
     * @param matchingTasks The list of tasks to display. Can be empty.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            printMessage(("No matching tasks found!").indent(4));
        } else {
            printMessage(("Here are all the matching tasks I can find:").indent(4));
            for (int i = 0; i < matchingTasks.size(); i++) {
                printMessage(((i + 1) + ". " + matchingTasks.get(i)).indent(8));
            }
        }
    }

    /**
     * Executes a command and captures its output as a String for GUI display.
     * The output is simultaneously written to both the console (for CLI visibility)
     * and captured for GUI display, ensuring both interfaces show the same information.
     * <p>
     * This method temporarily redirects "System.out" to a "tee" stream that duplicates
     * all output to both the original console and an internal capture buffer.
     * <p>
     *
     * @param command  The command to execute
     * @param taskList The task list to operate on
     * @param storage  The storage handler for persistence
     * @return The captured output as a String, trimmed of leading/trailing whitespace
     * @throws NeruneruneException if command execution fails
     */
    // implemented with help of AI
    public String executeAndCapture(Command command, TaskList taskList, Storage storage)
            throws NeruneruneException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        // create a "tee" stream that writes to both original output and capture
        PrintStream teeStream = new PrintStream(baos) {
            @Override
            public void write(int b) {
                super.write(b);
                originalOut.write(b);  // write to original System.out
            }

            @Override
            public void write(byte[] buf, int off, int len) {
                super.write(buf, off, len);
                originalOut.write(buf, off, len);  // write to original System.out
            }
        };

        System.setOut(teeStream);

        try {
            command.execute(taskList, this, storage);
            teeStream.flush();
            originalOut.flush();
            return baos.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }


}
