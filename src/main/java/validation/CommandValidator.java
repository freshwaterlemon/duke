package validation;

public class CommandValidator {
    // check if details after valid command is missing
    public static void validateCommandDetails(String command, String taskString) throws Exception {
        if (taskString.isEmpty()) {
            throw new Exception("Details after '" + command + "' cannot be empty.");
        }
    }

    // validate if the user input is empty
    public static boolean checkAndPrintIfEmpty(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            System.out.println(("Oops! You didn't type anything. Go ahead, give me a task!\n").indent(4));
            return true; // input is empty, notification printed
        }
        return false; // input is not empty
    }

}
