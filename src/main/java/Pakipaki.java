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
        printHorzLine();
        System.out.println("Thank you for chatting with me! Until next time, keep cracking and stay sharp!");
        printHorzLine();
    }

    // print items inside task list
    private static void printTaskList(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            System.out.println(
                    "   Your task list is empty, nothing snapped in yet! Add something and I'll keep it ready for you!");
        } else {
            System.out.println("Here's what's on your task list so far:");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + taskList.get(i));
            }
            System.out.println();
        }
    }

    // add task object to arraylist of task
    private static void addTask(String description, ArrayList<Task> taskList) {
        Task task = new Task(description);
        taskList.add(task);
        System.out.println("Snap! \"" + task.getDescription() + "\" added to your task list.\n");
    }

    // find if task is inside the tasklist arraylist
    private static Task findTaskByDescription(String description, ArrayList<Task> taskList, boolean doneStatus) {
        for (Task task : taskList) {
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
        Task task = findTaskByDescription(toDoString, taskList, false);
        if (task != null) {
            // check if already mark as done
            // if (!task.getIsDone()) {
            task.markAsDone();
            System.out.println("Wokay! \"" + toDoString + "\" mark as done!\n");
            System.out.println("    " + task + "\n");
            // } else {
            // System.out.println("Task \"" + toDoString + "\" is already marked as
            // done.\n");
            // }
        } else {
            System.out.println(toDoString + " not found or all matching tasks are already marked as done.\n");
        }
    }

    // handle unmarking of task as undone
    private static void handleUnmark(String toDoString, ArrayList<Task> taskList) {
        Task task = findTaskByDescription(toDoString, taskList, true);
        if (task != null) {
            // check if already unmark
            // if (task.getIsDone()) {
            task.markAsUndone();
            System.out.println("Alright \"" + toDoString + "\" unmark.\n");
            System.out.println("    " + task + "\n");
            // } else {
            // System.out.println("Task \"" + toDoString + "\" was already not marked as
            // done.\n");
            // }
        } else {
            System.out.println(toDoString + " not found or all matching tasks are already unmarked.\n");
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
            System.out.print("You want to: ");
            userInput = in.nextLine();
            userInput = userInput.trim();

            if (userInput.isEmpty()) {
                System.out.println("(Oops! You didn't type anything. Go ahead, give me a task!)\n");
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

                default:
                    addTask(userInput, taskList);
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
