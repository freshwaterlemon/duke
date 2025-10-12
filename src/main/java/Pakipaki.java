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

    // find if task is inside the tasklist arraylist
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList, boolean doneStatus,
            boolean findFromEnd) throws PakipakiException {
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
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList) throws PakipakiException {
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
    private static void handleMark(String taskString, ArrayList<Task> taskList) {
        try {
            Task task = findTaskByDescription(taskString, taskList, false, false);
            task.markAsDone();
            System.out.println(("Alright! \"" + taskString + "\" mark as done!").indent(4));
            System.out.println((task + "\n").indent(8));
            storage.saveTasksToStorage(taskList); // mark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // handle unmarking of task as undone
    private static void handleUnmark(String taskString, ArrayList<Task> taskList) {
        try {
            Task task = findTaskByDescription(taskString, taskList, true, true);
            task.markAsUndone();
            System.out.println(("Alright! \"" + taskString + "\" unmark.").indent(4));
            System.out.println((task + "\n").indent(8));
            storage.saveTasksToStorage(taskList); // unmark and save new tasklist to tasks.txt
        } catch (PakipakiException e) {
            System.out.println((e.getMessage() + "\n").indent(4));
        }
    }

    // add task object to arraylist of task and display confirmation message
    private static void addTask(Task task, ArrayList<Task> taskList) {
        taskList.add(task);
        System.out.println(("Got it, task added to your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        storage.saveTasksToStorage(taskList); // add and save new tasklist to tasks.txt
    }

    // handle to do
    private static void handleToDo(String description, ArrayList<Task> taskList) {
        // addTask(new Todo(description), taskList);
        try {
            Task todoTask = Parser.parseTodo(description);
            addTask(todoTask, taskList);
        } catch (Exception e) {
            ui.printMessage(("Error parsing todo: " + e.getMessage()).indent(4));
        }
    }

    // handle deadline
    private static void handleDeadline(String description, ArrayList<Task> taskList) {
        // int findIndex = description.indexOf("/by");
        // if (findIndex == -1) {
        // System.out.println(("Oops! The deadline detail is missing").indent(4));
        // System.out.println(("Please use the format: deadline <task> /by
        // <date/time>").indent(4));
        // return;
        // }
        // String deadlineDescription = description.substring(0, findIndex).trim();
        // String deadlineTiming = description.substring(findIndex + 3).trim();
        // addTask(new Deadline(deadlineDescription, deadlineTiming), taskList);
        try {
            Task deadlineTask = Parser.parseDeadline(description);
            addTask(deadlineTask, taskList);
        } catch (Exception e) {
            ui.printMessage(("Error parsing deadline: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: deadline <task> /by <date/time>").indent(4));
        }
    }

    // handle event
    private static void handleEvent(String description, ArrayList<Task> taskList) {
        // int findIndexFrom = description.indexOf("/from");
        // int findIndexTo = description.indexOf("/to");
        // // user must include both from and to
        // if (findIndexFrom == -1 || findIndexTo == -1 || findIndexFrom > findIndexTo)
        // {
        // System.out.println(("Oops! The event detail is missing").indent(4));
        // System.out.println(("Please use the format: event <task> /from <date/time>
        // /to <date/time>").indent(4));
        // return;
        // }
        // String eventDescription = description.substring(0, findIndexFrom).trim();
        // String eventTimingFrom = description.substring(findIndexFrom + 5,
        // findIndexTo).trim();
        // String eventTimingTo = description.substring(findIndexTo + 3).trim();
        // addTask(new Event(eventDescription, eventTimingFrom, eventTimingTo),
        // taskList);
        try {
            Task eventTask = Parser.parseEvent(description);
            addTask(eventTask, taskList);
        } catch (Exception e) {
            ui.printMessage(("Error parsing event: " + e.getMessage()).indent(4));
            ui.printMessage(("Please use the format: event <task> /from <date/time> /to <date/time>").indent(4));
        }
    }

    // delete task object from arraylist of task and display confirmation message
    private static void deleteTask(Task task, ArrayList<Task> taskList) {
        taskList.remove(task);
        System.out.println(("Got it, task removed from your task list.").indent(4));
        System.out.println((task.toString() + "\n").indent(8));
        System.out.println(("Now you have " + taskList.size() + " tasks in the list.\n").indent(4));

        storage.saveTasksToStorage(taskList); // delete and save new tasklist to tasks.txt
    }

    // handle delete
    private static void handleDelete(String description, ArrayList<Task> taskList) throws PakipakiException {
        Task task = findTaskByDescription(description, taskList);
        deleteTask(task, taskList);
    }

    // check if details after valid command is missing
    private static void validateCommandDetails(String command, String taskString) throws PakipakiException {
        if (taskString.isEmpty()) {
            throw new PakipakiException("Details after '" + command + "' cannot be empty.");
        }
    }

    // get user input and display them
    public static void handleUserInput(Scanner in) {

        ArrayList<Task> taskList = new ArrayList<Task>(); // arraylist of task object to store all task dynamically
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

                // // get the first word in the sentance
                // String command = userInput.split(" ")[0].toLowerCase();
                // // get everything after first word if there are more after first word
                // String taskString = userInput.length() > command.length() ?
                // userInput.substring(command.length()).trim()
                // : "";

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
                        handleMark(taskString, taskList);
                        break;

                    case "unmark":
                        validateCommandDetails(command, taskString);
                        handleUnmark(taskString, taskList);
                        break;

                    case "todo":
                        validateCommandDetails(command, taskString);
                        handleToDo(taskString, taskList);
                        break;

                    case "deadline":
                        validateCommandDetails(command, taskString);
                        handleDeadline(taskString, taskList);
                        break;

                    case "event":
                        validateCommandDetails(command, taskString);
                        handleEvent(taskString, taskList);
                        break;

                    case "delete":
                        validateCommandDetails(command, taskString);
                        handleDelete(taskString, taskList);
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