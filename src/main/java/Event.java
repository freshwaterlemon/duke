public class Event extends Task {

    public Event(String eventDescription) {
        super(eventDescription);
    }

    public String getScheduleItem() {
        return "event";
    }

    @Override
    public String toString() {
        return "[E]" + "[" + getStatusIcon() + "] " + description;
    }
}
