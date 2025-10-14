package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class DeleteCommand extends Command {
    private final String taskString;

    public DeleteCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.deleteTask(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
