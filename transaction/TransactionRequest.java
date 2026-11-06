package transaction;

public class TransactionRequest implements Comparable<TransactionRequest> {
    private static int counter = 1;

    private final int id;
    private int accountNumber;
    private final RequestType type;
    private final int amount;
    private final int priority;     // bigger number = served earlier
    private String status;          // PENDING, DONE, REJECTED

    public TransactionRequest(int accountNumber, RequestType type, int amount, int priority) {
        this.id = counter++;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.priority = priority;
        this.status = "PENDING";
    }

    public int getId() { return id; }
    public int getAccountNumber() { return accountNumber; }
    public RequestType getType() { return type; }
    public int getAmount() { return amount; }
    public int getPriority() { return priority; }
    public String getStatus() { return status; }

    public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }
    public void setStatus(String status) { this.status = status; }

    // higher priority first, if equal - FIFO by id
    @Override
    public int compareTo(TransactionRequest other) {
        int result = Integer.compare(other.priority, priority);
        if (result != 0) {
            return result;
        }
        return Integer.compare(id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionRequest)) return false;
        return id == ((TransactionRequest) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Request #" + id + " [" + type + " " + amount + " $] account " + accountNumber
                + ", priority " + priority + ", status " + status;
    }
}
