import java.util.Scanner;
import ui.Ui;
import storage.Storage;
import parser.Parser;

import task.Task;

import java.util.ArrayList;

public class Pakipaki {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILEPATH);
    private static final ArrayList<Task> taskList = new ArrayList<>();

    // find if task is inside the tasklist arraylist
    // private static Task findTaskByDescription(String description, ArrayList<Task>
    // taskList, boolean doneStatus,
    private static Task findTaskByDescription(String description, boolean doneStatus, boolean findFromEnd)
            throws PakipakiException {
        // unmark from the end of list as newer task should be unmark first
        int startFrom, endAt, step;

        if (findFromEnd) {
            startFrom = taskList.size() - 1;
            endAt = -1;
            step = -1;
        } else {
            startFrom = 0;
            endAt = taskList.size();
            step = 1;
        }

        for (int i = startFrom; i != endAt; i += step) {
            Task task = taskList.get(i);
            // add in && task.getIsDone() == doneStatus to handle duplicate task
            // user decide to do the same task again after it is done
            // check isDone in task obj, continue if task is found but already mark as done
            if (task.getDescription().equalsIgnoreCase(description) && task.getIsDone() == doneStatus) {
                return task;
            }
        }
        // return null;
        throw new PakipakiException(
                "Task with description \"" + description + "\" not found.");

    }

    /*
     * find if task is inside the tasklist arraylist using
     * description or by using index
     */
    // private static Task findTaskByDescription(String description, ArrayList<Task>
    // taskList) throws PakipakiException {
    private static Task findTaskByDescription(String description) throws PakipakiException {
        try {
            int taskIndex = Integer.parseInt(description) - 1;
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new PakipakiException("No task number: " + (taskIndex + 1));
            }
            return taskList.get(taskIndex);
        } catch (NumberFormatException e) {
            // no number provided, fall back to description search
        }

        for (Task task : taskList) {
            if (task.getDescription().equalsIgnoreCase(description)) {
                return task;
            }
        }

        throw new PakipakiException("Task with description \"" + description + "\" not found.");
    }

    // handle marking of task as done
    // private static void handleMark(String taskString, ArrayList<Task> taskList) {
    private static void handleMark(String taskString) {
        try {
            // Task task = findTaskByDescription(taskString, taskList, false, false);
            Task task = findTaskByDescription(taskString, false, false);
            task.markAsDone();
            System.out.println(("Alright! \"" + taskString + "\" mark as done!").indent(4));
            System.out.println((task + "\n").indent(8));
            storage.saveTasksToStorage(taskList); // mark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // handle unmarking of task as undone
    // private static void handleUnmark(String taskString, ArrayList<Task> taskList)
    // {
    private static void handleUnmark(String taskString) {
        try {
            // Task task = findTaskByDescription(taskString, taskList, true, true);
            Task task = findTaskByDescription(taskString, true, true);
            task.markAsUndone();
            System.out.println(("Alright! \"" + taskString + "\" unmark.").indent(4));
            System.out.println((task + "\n").indent(8));
            storage.saveTasksToStorage(taskList); // unmark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // add task object to arraylist of task and display confirmation message
    // private static void addTask(Task task, ArrayList<Task> taskList) {
    private static void addTask(Task task) {
        taskList.add(task);
        System.out.println(("Got it, task added to your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        storage.saveTasksToStorage(taskList); // add and save new tasklist to tasks.txt
    }

    // handle to do
    // private static void handleToDo(String description, ArrayList<Task> taskList)
    // {
    private static void handleToDo(String description) {
        // addTask(new Todo(description), taskList);
        try {
            Task todoTask = Parser.parseTodo(description);
            // addTask(todoTask, taskList);
            addTask(todoTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing todo: " + e.getMessage()).indent(4));
        }
    }

    // handle deadline
    // private static void handleDeadline(String description, ArrayList<Task>
    // taskList) {
    private static void handleDeadline(String description) {
        try {
            Task deadlineTask = Parser.parseDeadline(description);
            // addTask(deadlineTask, taskList);
            addTask(deadlineTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing deadline: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: deadline <task> /by <date/time>").indent(4));
        }
    }

    // handle event
    // private static void handleEvent(String description, ArrayList<Task> taskList)
    // {
    private static void handleEvent(String description) {
        try {
            Task eventTask = Parser.parseEvent(description);
            // addTask(eventTask, taskList);
            addTask(eventTask);
        } catch (Exception e) {
            ui.printMessage(("Error parsing event: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: event <task> /from <date/time> /to <date/time>").indent(4));
        }
    }

    // delete task object from arraylist of task and display confirmation message
    // private static void deleteTask(Task task, ArrayList<Task> taskList) {
    private static void deleteTask(Task task) {
        taskList.remove(task);
        System.out.println(("Got it, task removed from your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        storage.saveTasksToStorage(taskList); // delete and save new tasklist to tasks.txt
    }

    // handle delete
    // private static void handleDelete(String description, ArrayList<Task>
    // taskList) throws PakipakiException {
    private static void handleDelete(String description) throws PakipakiException {
        // Task task = findTaskByDescription(description, taskList);
        // deleteTask(task, taskList);
        Task task = findTaskByDescription(description);
        deleteTask(task);
    }

    // check if details after valid command is missing
    private static void validateCommandDetails(String command, String taskString) throws PakipakiException {
        if (taskString.isEmpty()) {
            throw new PakipakiException("Details after '" + command + "' cannot be empty.");
        }
    }

    // get user input and display them
    public static void handleUserInput(Scanner in) {

        // ArrayList<Task> taskList = new ArrayList<Task>(); // arraylist of task object
        // to store all task dynamically
        storage.handleStorage(taskList); // load tasklist from storage if availble

        // loop for user input
        while (true) {
            try {

                System.out.println();
                String userInput = in.nextLine().trim();

                if (userInput.isEmpty()) {
                    System.out.println(("Oops! You didn't type anything. Go ahead, give me a task!\n").indent(4));
                    continue;
                }

                // get the first word in the sentance
                String command = Parser.getCommand(userInput);
                // get everything after first word if there are more after first word
                String taskString = Parser.getArguments(userInput);

                switch (command) {
                    case "list":
                        ui.printTaskList(taskList);
                        break;

                    case "bye":
                        ui.endMsg();
                        return;

                    case "mark":
                        validateCommandDetails(command, taskString);
                        // handleMark(taskString, taskList);
                        handleMark(taskString);
                        break;

                    case "unmark":
                        validateCommandDetails(command, taskString);
                        // handleUnmark(taskString, taskList);
                        handleUnmark(taskString);
                        break;

                    case "todo":
                        validateCommandDetails(command, taskString);
                        // handleToDo(taskString, taskList);
                        handleToDo(taskString);
                        break;

                    case "deadline":
                        validateCommandDetails(command, taskString);
                        // handleDeadline(taskString, taskList);
                        handleDeadline(taskString);
                        break;

                    case "event":
                        validateCommandDetails(command, taskString);
                        // handleEvent(taskString, taskList);
                        handleEvent(taskString);
                        break;

                    case "delete":
                        validateCommandDetails(command, taskString);
                        // handleDelete(taskString, taskList);
                        handleDelete(taskString);
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
            handleUserInput(in);
        }
    }
}