package account;

import java.util.Scanner;

public class CreditAccount extends Account {
    private double debt;
    protected int creditLimit;
    protected double debtProcent;

    public CreditAccount(String name, String surname, int accountNumber, int quantityOfMoney, int phoneNumber, int creditLimit, double debtProcent) {
        super(name, surname, accountNumber, quantityOfMoney, phoneNumber);
        this.creditLimit = creditLimit;
        this.debtProcent = debtProcent;
        this.debt = 0;
    }
    
    // getters and setters
    public int getCreditLimit() { return creditLimit; }
    public double getDebtProcent() { return debtProcent; }
    public double getDebt() { return debt; }
    public void setCreditLimit(int creditLimit) { this.creditLimit = creditLimit; }
    public void setDebtProcent(double debtProcent) { this.debtProcent = debtProcent; }

    @Override
    public void displayInfo() {
        System.out.println("====================================");
        System.out.println("[Credit Account]");
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Account number: " + accountNumber);
        System.out.println("Quantity of money: " + quantityOfMoney + " $");
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Credit limit: " + creditLimit);
        System.out.println("Debt procent: " + debtProcent);
        System.out.println("Current debt: " + debt + " $");
        System.out.println("====================================");
    }

    @Override
    public void withdraw(Scanner sc) {
        System.out.println("Enter amount of withdraw: ");
        int amount = sc.nextInt();
        withdrawMoney(amount);
    }

    @Override
    public boolean withdrawMoney(int amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return false;
        }

        double available = quantityOfMoney + creditLimit - debt;
        if (amount > available) {
            System.out.println("Operation canceled! Exceeds available funds (" + available + " $)");
            return false;
        }

        if (amount <= quantityOfMoney) {
            quantityOfMoney -= amount;
        } else {
            int remainder = amount - quantityOfMoney;
            quantityOfMoney = 0;
            debt += remainder;
        }
        addTransaction("WITHDRAW", amount);
        System.out.println("Operation completed successfully! Withdrawn: " + amount + " $");
        return true;
    }

    @Override 
    public void deposit(Scanner sc) {
        System.out.println("Enter deposit amount: ");
        int sum = sc.nextInt();
        depositMoney(sum);
    }

    @Override
    public boolean depositMoney(int sum) {
        if (sum <= 0) {
            System.out.println("Amount must be positive!");
            return false;
        }
        quantityOfMoney += sum;
        addTransaction("DEPOSIT", sum);
        System.out.println("Success!");
        return true;
    }

    public void payOffLoan(Scanner sc) {
        if (debt <= 0) {
            System.out.println("You have no debt!");
            return;
        }
        System.out.println("Enter sum to pay off loan (your debt: " + debt + " $): ");
        int payment = sc.nextInt();

        if (payment <= 0) {
            System.out.println("Payment must be positive!");
            return;
        }
        if (payment > quantityOfMoney) {
            System.out.println("Not enough money on your account!");
            return;
        }
        quantityOfMoney -= payment;
        debt -= payment;
        if (debt < 0) {
            quantityOfMoney += (int) -debt;
            debt = 0;
        }
        addTransaction("LOAN_PAYMENT", payment);
        System.out.println("Payment accepted. Remaining debt: " + debt + " $");
    }
}
