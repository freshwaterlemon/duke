public class Event extends Task {
    protected String eventTimingFrom;
    protected String eventTimingTo;

    public Event(String eventDescription, String eventTimingFrom, String eventTimingTo) {
        super(eventDescription);
        this.eventTimingFrom = eventTimingFrom;
        this.eventTimingTo = eventTimingTo;
    }

    public String getScheduleItem() {
        return "event";
    }

    @Override
    public String toString() {
        return "[E]" + "[" + getStatusIcon() + "] " + description + " (from: " + eventTimingFrom + " to: "
                + eventTimingTo
                + ")";
    }
}
