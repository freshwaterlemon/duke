public class Deadline extends Task {

    public Deadline(String deadlineDescription) {
        super(deadlineDescription);
    }

    public String getScheduleItem() {
        return "deadline";
    }

    @Override
    public String toString() {
        return "[D]" + "[" + getStatusIcon() + "] " + description;
    }
}
