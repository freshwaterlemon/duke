package command;

import storage.Storage;
import tasklist.TaskList;
import ui.Ui;

public class ViewAllCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printMessage(ui.getUserGuideMsg());
    }
}
