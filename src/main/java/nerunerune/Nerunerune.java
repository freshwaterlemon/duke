import java.io.IOException;
import java.util.Scanner;
import command.Command;
import exception.NeruneruneException;
import parser.Parser;
import ui.Ui;
import storage.Storage;
import tasklist.TaskList;

/**
 * Main class for the Nerunerune application.
 * Manages the overall program flow including initializing components,
 * loading tasks from storage, and processing user commands.
 */
public class Nerunerune {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Constructs a Nerunerune instance with the specified storage file path.
     * Initializes the UI, storage handler, and task list.
     *
     * @param filePath the file path where tasks are stored
     */
    public Nerunerune(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList(storage, ui);
    }

    /**
     * Entry point of the application.
     * Creates a new Nerunerune instance with the default storage file path and runs it.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Nerunerune(DEFAULT_STORAGE_FILEPATH).run();
    }

    /**
     * Runs the main loop of the application.
     * Starts the UI, loads tasks, and continually accepts user input commands
     * until the exit command is given.
     * Handles exceptions by displaying appropriate messages.
     */
    public void run() {
        ui.startMsg();
        try {
            taskList.loadTasks();
        } catch (NeruneruneException | IOException e) {
            ui.printMessage("Storage error: " + e.getMessage());
        }

        boolean isExit = false;
        try (Scanner in = new Scanner(System.in)) {
            while (!isExit) {
                try {
                    ui.printLine();
                    String userInput = in.nextLine().trim();
                    Command userCommand = Parser.parseCommand(userInput, ui);
                    userCommand.execute(taskList, ui, storage);
                    isExit = userCommand.isExit();
                } catch (NeruneruneException e) {
                    ui.printMessage((e.getMessage()).indent(4));
                } catch (Exception e) {
                    ui.printMessage(("Something went wrong!: " + e.getMessage()).indent(4));
                }
            }
        }
    }
}