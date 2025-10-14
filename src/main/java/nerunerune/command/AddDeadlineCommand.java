package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class AddDeadlineCommand extends Command {
    private final String taskString;

    public AddDeadlineCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addDeadline(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
