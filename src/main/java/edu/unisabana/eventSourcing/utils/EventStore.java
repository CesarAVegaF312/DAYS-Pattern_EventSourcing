import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class EventStore {
    private final List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents(UUID aggregateId) {
        // For simplicity, we ignore the aggregateId and return all events.
        return events;
    }
}
