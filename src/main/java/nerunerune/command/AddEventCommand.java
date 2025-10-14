package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class AddEventCommand extends Command {
    private final String taskString;

    public AddEventCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.addEvent(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
