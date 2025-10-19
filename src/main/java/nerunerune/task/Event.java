package task;

import java.time.LocalDateTime;

import parser.DateTimeParser;
import parser.DateTimeParser;

public class Event extends Task {
    private LocalDateTime fromTiming;
    private LocalDateTime toTiming;

    public Event(String eventDescription, LocalDateTime eventFromTiming, LocalDateTime eventToTiming, boolean isDone) {
        super(eventDescription, isDone);
        this.fromTiming = eventFromTiming;
        this.toTiming = eventToTiming;
    }

    public Event(String eventDescription, LocalDateTime eventFromTiming, LocalDateTime eventToTiming) {
        super(eventDescription);
        this.fromTiming = eventFromTiming;
        this.toTiming = eventToTiming;

    }

    public Event(String eventDescription, boolean isDone) {
        super(eventDescription, isDone);
    }

    public String getScheduleItem() {
        return "event";
    }

    @Override
    public String toString() {
        return "[E]" + "[" + getStatusIcon() + "] " + getDescription() + " (from: "
                + DateTimeParser.formatForDisplay(fromTiming) + " to: " + DateTimeParser.formatForDisplay(toTiming) + ")";
    }

    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s | %s",
                getIsDone() ? 1 : 0,
                getDescription(),
                DateTimeParser.formatForStorage(fromTiming),
                DateTimeParser.formatForStorage(toTiming));
    }
}
