package transaction;

public class Transaction {
    private static int counter = 1;
    private final int id;
    private final String type;      // DEPOSIT, WITHDRAW, INTEREST, LOAN_PAYMENT
    private final int amount;
    private final int balanceAfter;

    public Transaction(String type, int amount, int balanceAfter) {
        this.id = counter++;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public int getAmount() { return amount; }
    public int getBalanceAfter() { return balanceAfter; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        return id == ((Transaction) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "#" + id + " " + type + " " + amount + " $ (balance: " + balanceAfter + " $)";
    }
}
