package account;

import transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public abstract class Account implements Depositable, Withdrawable, Comparable<Account> {
    protected String name;
    protected String surname;
    protected int accountNumber;
    protected int quantityOfMoney;
    protected int phoneNumber;
    //1:N (composition)
    protected List<Transaction> history = new ArrayList<>();

    public Account() {}

    public Account(String name, String surname, int accountNumber, int quantityOfMoney, int phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.accountNumber = accountNumber;
        this.quantityOfMoney = quantityOfMoney;
        this.phoneNumber = phoneNumber;
    }

    // getters
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getAccountNumber() { return accountNumber; }
    public int getQuantityOfMoney() { return quantityOfMoney; }
    public int getPhoneNumber() { return phoneNumber; }
    public List<Transaction> getHistory() { return Collections.unmodifiableList(history); }
    // account type name is used as key in category index
    public String getType() { return getClass().getSimpleName(); }

    // setters
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public void setAccountNumber(int accountNumber) {
        if (accountNumber <= 0) {
            System.out.println("ID can't be negative!");
            return;
        }
        this.accountNumber = accountNumber;
    }
    public void setQuantityOfMoney(int quantityOfMoney) {
        if (quantityOfMoney < 0) {
            System.out.println("Transaction failed! Balance cant be negative");
            return;
        }
        this.quantityOfMoney = quantityOfMoney;
    }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    // save operation to history (called after balance was changed)
    protected void addTransaction(String type, int amount) {
        history.add(new Transaction(type, amount, quantityOfMoney));
    }

    public void displayHistory() {
        System.out.println("====================================");
        System.out.println("[History of account " + accountNumber + "]");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        }
        for (Transaction t : history) {
            System.out.println(t);
        }
        System.out.println("====================================");
    }

    public abstract void displayInfo();
    public abstract void withdraw(Scanner sc);
    public abstract void deposit(Scanner sc);

    //the same operations without Scanner, they are used by request queue
    public abstract boolean withdrawMoney(int amount);
    public abstract boolean depositMoney(int amount);

    //accounts are equal if they have the same account number
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account other = (Account) o;
        return accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(accountNumber);
    }

    //by account number
    @Override
    public int compareTo(Account other) {
        return Integer.compare(accountNumber, other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + getType() + " | " + name + " " + surname + " | " + quantityOfMoney + " $";
    }
}
