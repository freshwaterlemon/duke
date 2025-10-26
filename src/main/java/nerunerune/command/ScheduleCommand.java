package nerunerune.command;

import nerunerune.exception.NeruneruneException;
import nerunerune.storage.Storage;
import nerunerune.tasklist.TaskList;
import nerunerune.ui.Ui;
import java.time.LocalDate;

/**
 * Represents a command to view the schedule for a specific date.
 * When executed, displays all tasks (deadlines and events) scheduled for the given date.
 */
public class ScheduleCommand extends Command {
    private final LocalDate date;

    /**
     * Constructs a ScheduleCommand with the specified date.
     *
     * @param date the date for which to display the schedule
     */
    public ScheduleCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Executes the schedule command by displaying tasks scheduled for the specified date.
     * Delegates to the UI to format and show the schedule view.
     *
     * @param tasks the task list containing all tasks
     * @param ui the user interface to display the schedule
     * @param storage the storage handler (not used in this command)
     * @throws NeruneruneException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NeruneruneException {
        ui.showSchedule(tasks, date);
    }
}