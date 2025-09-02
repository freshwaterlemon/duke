import java.util.Scanner;
import java.util.ArrayList;

public class Pakipaki {
    private static final String BOTNAME = "PakiPaki";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    // print the horizontal decoration line
    public static void printHorzLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    // print start message
    public static void startMsg() {
        printHorzLine();
        System.out.println("Hello I am " + BOTNAME + ", your personal assistant ChatBot.");
        System.out.println("Feel free to tell me your tasks, I am here to help keep track of them!");
        System.out.println();
        System.out.println("You can:");
        System.out.println("1. Type your task to add it to the list.");
        System.out.println("2. Type 'list' to see all your saved tasks.");
        System.out.println("3. Type 'bye' to exit when you're done.");
        printHorzLine();
    }

    // print end message
    public static void endMsg() {
        printHorzLine();
        System.out.println("Thank you for chatting with me.\nGoodbye, and I look forward to our next conversation!");
        printHorzLine();
    }

    // print items inside task list
    private static void printTaskList(ArrayList<String> taskList) {
        if (taskList.isEmpty()) {
            System.out.println("   Your task list is currently empty. Add some tasks and come back to see them here!");
        } else {
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + taskList.get(i));
            }
            System.out.println();
        }
    }

    // get user input and display them
    /*
     * AI give suggestion to create Scanner outside of method
     * for better resource management
     */
    public static void handleUserInput(Scanner in) {
        String userInput;
        // arraylist to store user input dynamically
        ArrayList<String> taskList = new ArrayList<String>();

        // loop for user input
        while (true) {
            System.out.println();
            System.out.print("Input: ");
            userInput = in.nextLine();

            if (userInput.equalsIgnoreCase("list")) {
                printTaskList(taskList);

            } else if (userInput.equalsIgnoreCase("bye")) {
                // exit while loop and print goodbye message
                endMsg();
                break;
            } else if (!userInput.isEmpty()) {
                taskList.add(userInput);
                System.out.println("Task added: " + userInput + "\n");
            } else {
                System.out.println("(You entered an empty input.)\n");
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
