package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class UnmarkCommand extends Command {
    private final String taskString;

    public UnmarkCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.unmarkTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
