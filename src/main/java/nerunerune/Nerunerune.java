import java.util.Scanner;

import command.CommandHandler;
import ui.Ui;
import storage.Storage;

import tasklist.TaskList;

public class Nerunerune {
    private static final String DEFAULT_STORAGE_FILEPATH = "data/tasks.txt";

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(DEFAULT_STORAGE_FILEPATH);
    private static final TaskList taskList = new TaskList(storage, ui);

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) { // close scanner
            ui.startMsg();
            taskList.loadTasks();
            CommandHandler processor = new CommandHandler(ui, taskList, storage);
            processor.processUserCommands(in);
        }
    }
}