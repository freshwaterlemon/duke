package nerunerune.validation;

import nerunerune.exception.NeruneruneException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandValidatorTest {

    // validateCommandDetails tests
    @Test
    public void validateCommandDetails_validDetails_noExceptionThrown() {
        assertDoesNotThrow(() -> CommandValidator.validateCommandDetails("todo", "buy books"));
    }

    @Test
    public void validateCommandDetails_emptyDetails_throwsException() {
        Exception exception = assertThrows(Exception.class, () -> CommandValidator.validateCommandDetails("todo", ""));
        assertEquals("Details after 'todo' cannot be empty.", exception.getMessage());
    }

    @Test
    public void validateCommandDetails_markCommandEmpty_throwsException() {
        Exception exception = assertThrows(Exception.class, () -> CommandValidator.validateCommandDetails("mark", ""));
        assertEquals("Details after 'mark' cannot be empty.", exception.getMessage());
    }

    @Test
    public void validateCommandDetails_deleteCommandEmpty_throwsException() {
        Exception exception = assertThrows(Exception.class, () -> CommandValidator.validateCommandDetails("delete", ""));
        assertEquals("Details after 'delete' cannot be empty.", exception.getMessage());
    }

    // validateUserInputNotEmpty tests
    @Test
    public void validateUserInputNotEmpty_validInput_noExceptionThrown() {
        assertDoesNotThrow(() -> CommandValidator.validateUserInputNotEmpty("todo buy books"));
    }

    @Test
    public void validateUserInputNotEmpty_nullInput_throwNeruneruneException() {
        Exception exception = assertThrows(NeruneruneException.class, () -> CommandValidator.validateUserInputNotEmpty(null));
        assertTrue(exception.getMessage().contains("Oops! You didn't type anything"));
    }

    @Test
    public void validateUserInputNotEmpty_emptyString_throwNeruneruneException() {
        Exception exception = assertThrows(NeruneruneException.class, () -> CommandValidator.validateUserInputNotEmpty(""));
        assertTrue(exception.getMessage().contains("Oops! You didn't type anything"));
    }

    @Test
    public void validateUserInputNotEmpty_whitespaceOnly_throwNeruneruneException() {
        Exception exception = assertThrows(NeruneruneException.class, () -> CommandValidator.validateUserInputNotEmpty("   "));
        assertTrue(exception.getMessage().contains("Oops! You didn't type anything"));
    }

    @Test
    public void validateUserInputNotEmpty_tabsAndSpaces_throwNeruneruneException() {
        Exception exception = assertThrows(NeruneruneException.class, () -> CommandValidator.validateUserInputNotEmpty("\t  \n  "));
        assertTrue(exception.getMessage().contains("Oops! You didn't type anything"));
    }

    @Test
    public void validateUserInputNotEmpty_singleCharacter_noExceptionThrown() {
        assertDoesNotThrow(() -> CommandValidator.validateUserInputNotEmpty("a"));
    }
}
