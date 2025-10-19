package task;

public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String getScheduleItem() {
        return "todo";
    }

    @Override
    public String toString() {
        return "[T]" + "[" + getStatusIcon() + "] " + getDescription();
    }

    @Override
    public String toStorageString() {
        return String.format("T | %d | %s", getIsDone() ? 1 : 0, getDescription());
    }

}
