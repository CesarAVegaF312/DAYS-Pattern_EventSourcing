# Taller de Event Sourcing

## Descripción
Este taller tiene como objetivo enseñarte a implementar el patrón Event Sourcing utilizando un sistema de gestión de cuentas bancarias. A lo largo de este taller, crearás y trabajarás con eventos que representen operaciones en las cuentas, como depósitos y retiros, y aprenderás a reconstruir el estado de las cuentas basándote en estos eventos.

## Objetivos
- Entender y aplicar el patrón de diseño Event Sourcing.
- Implementar y manejar eventos en un sistema.
- Reconstituir el estado de un sistema a partir de eventos almacenados.

## Requisitos
- Java 8 o superior
- IDE para Java (IntelliJ IDEA, Eclipse, VSCode, etc.)

## Estructura del Proyecto propuesta
- `src/model/`: Clases de eventos.
- `src/service/`: Implementación del almacén de eventos y servicios de cuenta.
- `src/domain/`: Implementación de la clase que reconstituye el estado de la cuenta.
- `Main.java`: Clase principal para ejecutar el ejemplo.

## Ejemplo
En este repositorio, encontrarás otro proyecto que aborda una temática similar, el cual puede servir como referencia adicional.

## Instrucciones

### Paso 1: Clonar el Repositorio
Clona este repositorio en tu máquina local:

```bash
git clone https://github.com/tuusuario/event-sourcing-workshop.git
cd event-sourcing-workshop
```

### Paso 2: Definir Eventos

#### 2.1 Clase Base `AccountEvent`
Esta es una clase abstracta que actúa como base para todos los eventos relacionados con una cuenta bancaria.

```java
import java.time.LocalDateTime;

abstract class AccountEvent {
    private String accountId;
    private LocalDateTime timestamp;

    public AccountEvent(String accountId) {
        this.accountId = accountId;
        this.timestamp = LocalDateTime.now();  // Se captura el momento en que el evento ocurre
    }

    public String getAccountId() {
        return accountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
```

- **accountId**: Identificador único de la cuenta asociada con el evento.
- **timestamp**: Momento exacto en que ocurre el evento.

#### 2.2 Evento `DepositMoney`
Este evento representa un depósito en la cuenta.

```java
import java.math.BigDecimal;

class DepositMoney extends AccountEvent {
    private BigDecimal amount;

    public DepositMoney(String accountId, BigDecimal amount) {
        super(accountId);
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
```

- **amount**: Cantidad de dinero que se deposita en la cuenta.

#### 2.3 Evento `WithdrawMoney`
Este evento representa un retiro de dinero de la cuenta.

```java
import java.math.BigDecimal;

class WithdrawMoney extends AccountEvent {
    private BigDecimal amount;

    public WithdrawMoney(String accountId, BigDecimal amount) {
        super(accountId);
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
```

- **amount**: Cantidad de dinero que se retira de la cuenta.

### Paso 3: Implementar el Event Store

El `EventStore` es responsable de almacenar todos los eventos generados. Este almacén es clave en el patrón Event Sourcing, ya que guarda cada cambio en el sistema como un evento.

```java
import java.util.ArrayList;
import java.util.List;

class EventStore {
    private List<AccountEvent> events = new ArrayList<>();

    // Guarda un nuevo evento en el almacén
    public void saveEvent(AccountEvent event) {
        events.add(event);
    }

    // Recupera todos los eventos asociados con una cuenta específica
    public List<AccountEvent> getEvents(String accountId) {
        List<AccountEvent> accountEvents = new ArrayList<>();
        for (AccountEvent event : events) {
            if (event.getAccountId().equals(accountId)) {
                accountEvents.add(event);
            }
        }
        return accountEvents;
    }
}
```

### Paso 4: Implementar el Servicio de Cuenta

El `AccountService` maneja las operaciones de negocio, como depósitos y retiros. Este servicio crea los eventos correspondientes y los almacena en el `EventStore`.

```java
import java.math.BigDecimal;

class AccountService {
    private EventStore eventStore;

    public AccountService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    // Método para depositar dinero y generar un evento de depósito
    public void deposit(String accountId, BigDecimal amount) {
        DepositMoney deposit = new DepositMoney(accountId, amount);
        eventStore.saveEvent(deposit);
    }

    // Método para retirar dinero y generar un evento de retiro
    public void withdraw(String accountId, BigDecimal amount) {
        WithdrawMoney withdraw = new WithdrawMoney(accountId, amount);
        eventStore.saveEvent(withdraw);
    }
}
```

### Paso 5: Reconstituir el Estado de la Cuenta

La clase `Account` se encarga de reconstruir el estado de la cuenta bancaria aplicando los eventos almacenados en el `EventStore`.

```java
import java.math.BigDecimal;
import java.util.List;

class Account {
    private String accountId;
    private BigDecimal balance;

    public Account(String accountId, List<AccountEvent> events) {
        this.accountId = accountId;
        this.balance = BigDecimal.ZERO;  // Inicializamos el balance en cero
        for (AccountEvent event : events) {
            apply(event);
        }
    }

    // Aplica un evento al estado actual de la cuenta
    private void apply(AccountEvent event) {
        if (event instanceof DepositMoney) {
            this.balance = this.balance.add(((DepositMoney) event).getAmount());
        } else if (event instanceof WithdrawMoney) {
            this.balance = this.balance.subtract(((WithdrawMoney) event).getAmount());
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
```

### Paso 6: Probar el Sistema

Ahora que todo está implementado, vamos a probar el sistema con un ejemplo en la clase `Main`.

```java
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        EventStore eventStore = new EventStore();
        AccountService accountService = new AccountService(eventStore);

        String accountId = "12345";

        // Realizar operaciones en la cuenta
        accountService.deposit(accountId, new BigDecimal("100.00"));
        accountService.withdraw(accountId, new BigDecimal("30.00"));
        accountService.deposit(accountId, new BigDecimal("50.00"));

        // Reconstituir la cuenta desde los eventos
        Account account = new Account(accountId, eventStore.getEvents(accountId));
        System.out.println("El balance actual de la cuenta es: " + account.getBalance());
    }
}
```

### Paso 7: Desafíos y Extensiones
- Implementa un nuevo tipo de evento, por ejemplo, "Transferir Dinero" entre dos cuentas.
- Añade lógica para manejar fallos, como intentos de retiro cuando no hay suficiente balance.

## Conclusión
Este taller te ha guiado a través de los conceptos y la implementación del patrón Event Sourcing. Ahora deberías tener una comprensión sólida de cómo y cuándo aplicar este patrón en tus propios proyectos.
