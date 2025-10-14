package validation;

import exception.NeruneruneException;

public class CommandValidator {
    // check if details after valid command is missing
    public static void validateCommandDetails(String command, String taskString) throws Exception {
        if (taskString.isEmpty()) {
            throw new Exception("Details after '" + command + "' cannot be empty.");
        }
    }

    public static void validateUserInputNotEmpty(String userInput) throws NeruneruneException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new NeruneruneException(("Oops! You didn't type anything. Go ahead, give me a task!\n").indent(4));
        }
    }

    // public static boolean isUserInputEmpty(String userInput) {
    // return userInput == null || userInput.trim().isEmpty();
    // }

    // // validate if the user input is empty
    // public static boolean checkAndPrintIfEmpty(String userInput) {
    // if (isUserInputEmpty(userInput)) {
    // System.out.println(("Oops! You didn't type anything. Go ahead, give me a
    // task!\n").indent(4));
    // return true; // input is empty, notification printed
    // }
    // return false; // input is not empty
    // }

}
