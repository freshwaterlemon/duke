package task;
public class Deadline extends Task {
    private String byTiming;

    public Deadline(String deadlineDescription, String byTiming) {
        super(deadlineDescription);
        this.byTiming = byTiming;
    }

    public Deadline(String deadlineDescription, boolean isDone) {
        super(deadlineDescription, isDone);
    }

    public Deadline(String deadlineDescription, String byTiming, boolean isDone) {
        super(deadlineDescription, isDone);
        this.byTiming = byTiming;
    }

    public String getScheduleItem() {
        return "deadline";
    }

    @Override
    public String toString() {
        String formattedDeadlineDescription = description + " (by: " + byTiming + ")";
        // return "[D]" + "[" + getStatusIcon() + "] " + description;
        return "[D]" + "[" + getStatusIcon() + "] " + formattedDeadlineDescription;
    }

    @Override
    public String toStorageString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + byTiming;
    }
}
