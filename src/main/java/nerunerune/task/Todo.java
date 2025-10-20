package task;

/**
 * Represents a Todo task without any specific timing.
 * Extends the Task class to provide Todo-specific behavior.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the given description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Constructs a Todo task with the given description and completion status.
     *
     * @param description the description of the todo task
     * @param isDone      whether the task is marked as done
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Returns the schedule item type for this task.
     *
     * @return the string "todo"
     */
    @Override
    public String getScheduleItem() {
        return "todo";
    }

    /**
     * Returns a string representation of the todo task for display,
     * including type, status icon, and description.
     *
     * @return display string of the todo
     */
    @Override
    public String toString() {
        return "[T]" + "[" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * Returns a string suitable for storage, including task type,
     * done status, and description.
     *
     * @return storage string of the todo
     */
    @Override
    public String toStorageString() {
        return String.format("T | %d | %s", getIsDone() ? 1 : 0, getDescription());
    }

}
