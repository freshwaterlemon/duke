public abstract class Task {
    protected String description;
    protected boolean isDone;
    // protected String scheduleItem;

    // public Task(String description, String scheduleItem) {
    // this.description = description;
    // this.isDone = false;
    // this.scheduleItem = scheduleItem;
    // }
    public Task(String description) {
        this.description = description;
        this.isDone = false;

    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsDone() {
        return isDone;
    }

    // public String getScheduleItem() {
    // return scheduleItem;
    // }
    public abstract String getScheduleItem();

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    @Override
    public abstract String toString();

    // @Override
    // public String toString() {
    // return "[" + getStatusIcon() + "] " + description;
    // // switch (getScheduleItem()) {
    // // case "todo":
    // // return "[T]" + "[" + getStatusIcon() + "] " + description;
    // // case "deadline":
    // // return "[D]" + "[" + getStatusIcon() + "] " + description;
    // // case "event":
    // // return "[E]" + "[" + getStatusIcon() + "] " + description;
    // // default:
    // // throw new IllegalStateException("Unexpected schedule item: " +
    // // getScheduleItem());
    // // }
    // }

}
