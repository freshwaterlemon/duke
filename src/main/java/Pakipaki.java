import java.util.Scanner;
import java.util.ArrayList;

public class Pakipaki {
    private static final String BOTNAME = "PakiPaki";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    // print the horizontal decoration line
    private static void printHorzLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    // print start message
    private static void startMsg() {
        printHorzLine();
        String message = """
                Hello I am %s, your little assistant, here to help with your tasks

                Let's get things snapping:

                1. Type your task and I'll snap it onto your list.
                2. Type 'list' to see all your tasks lined up.
                3. Type 'bye' to exit when you're done.
                4. Type 'mark ...' followed by the task you want to mark to mark it as done.
                5. Type 'unmark ...' followed by the task you want to unmark to unmark it as done.
                """.formatted(BOTNAME);
        System.out.println(message);
        printHorzLine();
    }

    // print end message
    private static void endMsg() {
        // printHorzLine();
        System.out.println("Thank you for chatting with me! Until next time!");
        printHorzLine();
    }

    // print items inside task list
    private static void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println(
                    "   Your task list is empty! Add something and I'll keep it ready for you!");
        } else {
            System.out.println("    Here's what's on your task list so far:");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println("        " + (i + 1) + ". " + taskList.get(i));
            }
            System.out.println();
        }
    }

    // add task object to arraylist of task
    // private static void addTask(String description, String scheduleItem,
    // ArrayList<Task> taskList) {
    // Task task = new Task(description, scheduleItem);
    // taskList.add(task);
    // System.out.println("Snap! \"" + task.getDescription() + "\" added to your
    // task list.\n");
    // }

    // find if task is inside the tasklist arraylist
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList, boolean doneStatus,
            boolean findFromEnd) {
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
        return null;
    }

    // handle marking of task as done
    private static void handleMark(String toDoString, ArrayList<Task> taskList) {
        Task task = findTaskByDescription(toDoString, taskList, false, false);
        if (task != null) {
            task.markAsDone();
            System.out.println("    Wokay! \"" + toDoString + "\" mark as done!");
            System.out.println("        " + task + "\n");
        } else {
            System.out.println(toDoString + " not found or all matching tasks are already marked as done.\n");
        }
    }

    // handle unmarking of task as undone
    private static void handleUnmark(String toDoString, ArrayList<Task> taskList) {
        Task task = findTaskByDescription(toDoString, taskList, true, true);
        if (task != null) {
            task.markAsUndone();
            System.out.println("    Alright \"" + toDoString + "\" unmark.");
            System.out.println("        " + task + "\n");
        } else {
            System.out.println(toDoString + " not found or all matching tasks are already unmarked.\n");
        }
    }

    // handle to do
    private static void handleToDo(String description, String scheduleItem, ArrayList<Task> taskList) {
        Task task = new Task(description, scheduleItem);
        taskList.add(task);
        System.out.println("    " + task.getDescription() + " added to your task list.");
        System.out.println("        " + "[T][ ] " + task.getDescription());
        System.out.println("    Now you have " + taskList.size() + " tasks in the list.\n");
    }

    // handle deadline
    private static void handleDeadline(String description, String scheduleItem, ArrayList<Task> taskList) {
        int findIndex = description.indexOf("/by");
        if (findIndex != -1) {
            String deadlineDescription = description.substring(0, findIndex).trim();
            String deadlineTiming = description.substring(findIndex + 3).trim();
            String formattedDeadlineDetails = deadlineDescription + " (by: " + deadlineTiming + ")";

            Task task = new Task(formattedDeadlineDetails, scheduleItem);
            taskList.add(task);
            System.out.println("    " + task.getDescription() + " added to your task list.");
            System.out.println("        " + "[D][ ] " + task.getDescription());
            System.out.println("    Now you have " + taskList.size() + " tasks in the list.\n");
        } else {
            System.out.println(
                    "Oops! The deadline detail is missing.\nPlease use the format: deadline <task> /by <date/time>");
            return;
        }

    }

    // handle event
    private static void handleEvent(String description, String scheduleItem, ArrayList<Task> taskList) {
        int findIndexFrom = description.indexOf("/from");
        int findIndexTo = description.indexOf("/to");
        if (findIndexFrom != -1 && findIndexTo != -1) {
            String eventDescription = description.substring(0, findIndexFrom).trim();
            String eventTimingFrom = description.substring(findIndexFrom + 5, findIndexTo).trim();
            String eventTimingTo = description.substring(findIndexTo + 3).trim();
            String formattedEventDetails = eventDescription + " (from: " + eventTimingFrom + " to: " + eventTimingTo
                    + ")";

            Task task = new Task(formattedEventDetails, scheduleItem);
            taskList.add(task);
            System.out.println("    " + task.getDescription() + " added to your task list.");
            System.out.println("        " + "[E][ ] " + task.getDescription());
            System.out.println("    Now you have " + taskList.size() + " tasks in the list.\n");
        } else {
            System.out.println(
                    "Oops! The event detail is missing.\nPlease use the format: event <task> /from <date/time> /to <date/time>");
            return;
        }

    }

    // get user input and display them
    /*
     * AI give suggestion to create Scanner outside of method
     * for better resource management
     */
    public static void handleUserInput(Scanner in) {
        String userInput;
        // arraylist of task object to store all user input for todo dynamically
        ArrayList<Task> taskList = new ArrayList<Task>();
        // loop for user input
        while (true) {
            System.out.println();
            // System.out.print("You want to: ");
            userInput = in.nextLine();
            userInput = userInput.trim();

            if (userInput.isEmpty()) {
                System.out.println("    (Oops! You didn't type anything. Go ahead, give me a task!)\n");
                continue;
            }

            // get the first work in the sentance
            String command = userInput.split(" ")[0].toLowerCase();
            // get everything after first word if there are more after first word
            String toDoString = userInput.length() > command.length() ? userInput.substring(command.length()).trim()
                    : "";

            switch (command) {
                case "list":
                    printTaskList(taskList);
                    break;

                case "bye":
                    endMsg();
                    return;

                case "mark":
                    handleMark(toDoString, taskList);
                    break;

                case "unmark":
                    handleUnmark(toDoString, taskList);
                    break;

                case "todo":
                    handleToDo(toDoString, command, taskList);
                    break;

                case "deadline":
                    handleDeadline(toDoString, command, taskList);
                    break;

                case "event":
                    handleEvent(toDoString, command, taskList);
                    break;

                default:
                    // addTask(userInput, taskList);
                    System.out.println(
                            "   Failed! Include todo, deadline, or event in front  so that I can organize your schedule.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        startMsg();
        handleUserInput(in);
        in.close();
    }
}
