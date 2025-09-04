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
                2. Type 'list' to hear all your tasks lined up.
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

    // add task to arraylist of task
    private static void addTask(String description, ArrayList<Task> taskList) {
        Task task = new Task(description);
        taskList.add(task);
        System.out.println("Snap! \"" + task.getDescription() + "\" added to your task list.\n");
    }

    private static void handleMark(String toDoString, ArrayList<Task> taskList) {
        // System.out.println(toDoString + "\n"); // test method call
    }

    private static void handleUnmark(String toDoString, ArrayList<Task> taskList) {
        // System.out.println(toDoString + "\n"); // test method call
    }

    // get user input and display them
    /*
     * AI give suggestion to create Scanner outside of method
     * for better resource management
     */
    public static void handleUserInput(Scanner in) {
        String userInput;
        // arraylist to store user input dynamically
        // ArrayList<String> taskList = new ArrayList<String>();
        ArrayList<Task> taskList = new ArrayList<Task>();
        // loop for user input
        while (true) {
            System.out.println();
            System.out.print("You want to: ");
            userInput = in.nextLine();

            if (userInput.isEmpty()) {
                System.out.println("(Oops! You didn't type anything. Go ahead, give me a task!)\n");
                continue;
            }

            String command = userInput.split(" ")[0].toLowerCase();
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
            // if (userIn
            // put.equalsIgnoreCase("list")) {
            // printTaskList(taskList);

            // } else if (userInput.equalsIgnoreCase("bye")) {
            // // exit while-loop and print goodbye message
            // endMsg();
            // break;
            // } else if (userInput.toLowerCase().startsWith("mark")) {
            // // task.markAsDone();
            // System.out.println("mark is working\n");
            // } else if (userInput.toLowerCase().startsWith("unmark")) {
            // // task.markAsDone();
            // System.out.println("unmark is working\n");
            // } else if (!userInput.isEmpty()) {
            // // taskList.add(userInput);
            // addTask(userInput, taskList);
            // } else {
            // System.out.println("(Oops! You didn't type anything. Go ahead, give me a
            // task!)\n");
            // continue;
            // }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        startMsg();
        handleUserInput(in);
        in.close();
    }
}
