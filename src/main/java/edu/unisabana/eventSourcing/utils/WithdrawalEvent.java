import java.time.LocalDateTime;
import java.util.UUID;

class WithdrawalEvent implements Event {
    private final UUID id;
    private final LocalDateTime timestamp;
    private final double amount;

    public WithdrawalEvent(double amount) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }
}