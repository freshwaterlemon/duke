public class Deadline extends Task {
    protected String deadlineTiming;
    // public Deadline(String description) {
    // super(description);
    // }

    public Deadline(String deadlineDescription, String deadlineTiming) {
        super(deadlineDescription);
        this.deadlineTiming = deadlineTiming;
    }

    // public String getDeadlineDetails() {
    // return deadlineTiming;
    // }

    public String getScheduleItem() {
        return "deadline";
    }

    @Override
    public String toString() {
        return "[D]" + "[" + getStatusIcon() + "] " + description + " (by: " + deadlineTiming + ")";
    }
}
