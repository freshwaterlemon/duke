package task;

import java.time.LocalDateTime;

import parser.DateTimeParser;

public class Deadline extends Task {
    private LocalDateTime byTiming;

    public Deadline(String deadlineDescription, LocalDateTime byTiming) {
        super(deadlineDescription);
        this.byTiming = byTiming;
    }

    public Deadline(String deadlineDescription, boolean isDone) {
        super(deadlineDescription, isDone);
    }

    public Deadline(String deadlineDescription, LocalDateTime byTiming, boolean isDone) {
        super(deadlineDescription, isDone);
        this.byTiming = byTiming;
    }

    public String getScheduleItem() {
        return "deadline";
    }

    @Override
    public String toString() {
        return "[D]" + "[" + getStatusIcon() + "] " + getDescription() + " (by: " + DateTimeParser.formatForDisplay(byTiming)
                + ")";
    }

    @Override
    public String toStorageString() {
        return String.format("D | %d | %s | %s",
                getIsDone() ? 1 : 0,
                getDescription(),
                DateTimeParser.formatForStorage(byTiming));
    }

    public LocalDateTime getDateTime() {
        return byTiming;
    }
}