import java.util.Scanner;

public class Pakipaki {
    private static final String BOTNAME = "PakiPaki";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    // Print the horizontal decoration line
    public static void printHorzLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    // Print start message
    public static void startMsg() {
        printHorzLine();
        System.out.println("Hello I am " + BOTNAME + ", your personal assistant ChatBot.");
        System.out.println("How can I help you today? (type 'bye' to exit)");
        printHorzLine();
    }

    // Print end message
    public static void endMsg() {
        printHorzLine();
        System.out.println("Thank you for chatting with me.\nGoodbye, and I look forward to our next conversation!");
        printHorzLine();
    }

    // Get user input and display them
    public static void handleUserInput() {
        String userInput;
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a task: ");
            userInput = in.nextLine();
            if (userInput.equalsIgnoreCase("bye")) {
                endMsg();
                break;
            }

            System.out.println("Task to remember: " + userInput + "\n");

        }
    }

    public static void main(String[] args) {
        startMsg();
        handleUserInput();
    }
}
