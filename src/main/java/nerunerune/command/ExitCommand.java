package command;

import tasklist.TaskList;
import ui.Ui;
import storage.Storage;

public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.endMsg();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
