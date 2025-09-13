public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String getScheduleItem() {
        return "todo";
    }

    @Override
    public String toString() {
        return "[T]" + "[" + getStatusIcon() + "] " + description;
    }
}
