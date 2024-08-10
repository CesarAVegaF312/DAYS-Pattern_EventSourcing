import java.util.List;
import java.util.UUID;

import org.w3c.dom.events.Event;

/***
 * Explicación
    Modelo de Evento: Definimos eventos DepositEvent y WithdrawalEvent que representan transacciones en la cuenta bancaria.
    Evento Store: Implementamos un EventStore simple que almacena eventos en memoria.
    Agregado de Cuenta Bancaria: BankAccount aplica eventos para cambiar su estado interno y puede reconstruir su estado a partir de una lista de eventos.
    Uso del Event Sourcing: Creamos una cuenta, realizamos algunas transacciones, almacenamos los eventos y luego reconstruimos el estado de la cuenta a partir de esos eventos.
 */
public class Main {
    public static void main(String[] args) {
        EventStore eventStore = new EventStore();
        UUID accountId = UUID.randomUUID();
        
        // Create a new bank account and perform some operations
        BankAccount account = new BankAccount();
        account.deposit(100);
        account.withdraw(30);

        // Store the events
        for (Event event : account.getChanges()) {
            eventStore.addEvent(event);
        }

        // Rebuild the account state from events
        BankAccount restoredAccount = new BankAccount();
        List<Event> history = eventStore.getEvents(accountId);
        restoredAccount.loadFromHistory(history);

        System.out.println("Restored account balance: " + restoredAccount.getBalance());
    }
}
