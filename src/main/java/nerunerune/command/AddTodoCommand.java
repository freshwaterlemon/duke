package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class AddTodoCommand extends Command {
    private final String taskString;

    public AddTodoCommand(String taskString) {
        this.taskString = taskString;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        tasks.addTodo(taskString);
        storage.saveTasksToStorage(tasks.getTaskList());
    }
}
