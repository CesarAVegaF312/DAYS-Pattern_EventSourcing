import java.time.LocalDateTime;
import java.util.UUID;

interface Event {
    UUID getId();
    LocalDateTime getTimestamp();
}