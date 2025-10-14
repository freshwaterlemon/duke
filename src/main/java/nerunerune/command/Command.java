package command;

import exception.NeruneruneException;
import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException;

    public boolean isExit() {
        return false;
    }
}
