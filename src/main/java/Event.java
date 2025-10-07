public class Event extends Task {
    private String fromTiming;
    private String toTiming;

    public Event(String eventDescription, String eventFromTiming, String eventToTiming, boolean isDone) {
        super(eventDescription, isDone);
        this.fromTiming = eventFromTiming;
        this.toTiming = eventToTiming;
    }

    public Event(String eventDescription, String eventFromTiming, String eventToTiming) {
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
        String formattedEventDescription = description + " (from: " + fromTiming + " to: " + toTiming
                + ")";
        // return "[E]" + "[" + getStatusIcon() + "] " + description;
        return "[E]" + "[" + getStatusIcon() + "] " + formattedEventDescription;
    }

    @Override
    public String toStorageString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + fromTiming + " | " + toTiming;
    }
}
