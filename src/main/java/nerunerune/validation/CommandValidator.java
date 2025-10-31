package nerunerune.validation;

import nerunerune.exception.NeruneruneException;

/**
 * Provides validation methods for user commands and input in the Nerunerune application.
 * Ensures commands contain necessary details and user inputs are not empty.
 */
public class CommandValidator {
    /**
     * Validates that the details following a command are not empty.
     *
     * @param command    the command word (e.g. "todo", "mark")
     * @param taskString the string containing task details after the command
     * @throws NeruneruneException if the task details are empty
     */
    public static void validateCommandDetails(String command, String taskString) throws NeruneruneException {
        if (taskString.isEmpty()) {
            throw new NeruneruneException("Details after '" + command + "' cannot be empty.");
        }
    }

    /**
     * Validates that the user input is not empty or null.
     *
     * @param userInput the full input string entered by the user
     * @throws NeruneruneException if input is empty or only whitespace
     */
    public static void validateUserInputNotEmpty(String userInput) throws NeruneruneException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new NeruneruneException("Oops! You didn't type anything\n");
        }
    }

}
