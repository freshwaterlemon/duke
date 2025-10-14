package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class MarkCommand extends Command {
    private final String taskString;

    public MarkCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.markTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
