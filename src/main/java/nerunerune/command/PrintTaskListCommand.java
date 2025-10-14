package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class PrintTaskListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.listTasks();
    }

}
