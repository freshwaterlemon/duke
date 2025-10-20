package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

/**
 * Represents a command to print the entire task list.
 * Executes the display of all tasks managed in the task list.
 */
public class PrintTaskListCommand extends Command {

    /**
     * Executes the command by listing all tasks in the given task list.
     *
     * @param tasks the task list to be displayed
     * @param ui the user interface
     * @param storage the storage component
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.listTasks();
    }

}
