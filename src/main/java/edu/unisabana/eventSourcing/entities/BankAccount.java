import java.util.List;

class BankAccount {
    private double balance = 0.0;
    private final List<Event> changes = new ArrayList<>();

    public void deposit(double amount) {
        applyChange(new DepositEvent(amount));
    }

    public void withdraw(double amount) {
        applyChange(new WithdrawalEvent(amount));
    }

    public double getBalance() {
        return balance;
    }

    private void applyChange(Event event) {
        if (event instanceof DepositEvent) {
            balance += ((DepositEvent) event).getAmount();
        } else if (event instanceof WithdrawalEvent) {
            balance -= ((WithdrawalEvent) event).getAmount();
        }
        changes.add(event);
    }

    public List<Event> getChanges() {
        return changes;
    }

    public void loadFromHistory(List<Event> history) {
        for (Event event : history) {
            applyChange(event);
        }
    }
}
