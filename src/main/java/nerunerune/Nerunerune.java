import java.util.Scanner;
import command.Command;
import exception.NeruneruneException;
import parser.Parser;
import ui.Ui;
import storage.Storage;
import tasklist.TaskList;

public class Nerunerune {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    // private static final Ui ui = new Ui();
    // private static final Storage storage = new Storage(DEFAULT_STORAGE_FILEPATH);
    // private static final TaskList taskList = new TaskList(storage, ui);
    private final Ui ui;
    private final Storage storage;
    private final TaskList taskList;

    public Nerunerune(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList(storage, ui);
    }

    public static void main(String[] args) {
        // try (Scanner in = new Scanner(System.in)) { // close scanner
        // ui.startMsg();
        // taskList.loadTasks();
        // CommandHandler processor = new CommandHandler(ui, taskList, storage);
        // processor.processUserCommands(in);
        // }
        new Nerunerune(DEFAULT_STORAGE_FILEPATH).run();

    }

    public void run() {
        ui.startMsg();
        taskList.loadTasks();

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