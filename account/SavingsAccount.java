package account;

import java.util.Scanner;

public class SavingsAccount extends Account{
    protected double incomePercent;

    public SavingsAccount(String name, String surname, int accountNumber, int quantityOfMoney, int phoneNumber, double incomePercent) {
        super(name, surname, accountNumber, quantityOfMoney, phoneNumber);
        this.incomePercent = incomePercent;
    }

    public double getIncomePercent() { return incomePercent; }
    public void setIncomePercent(double incomePercent) { this.incomePercent = incomePercent; }

    @Override
    public void displayInfo() {
        System.out.println("====================================");
        System.out.println("[Savings Account]");
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Account number: " + accountNumber);
        System.out.println("Quantity of money: " + quantityOfMoney + " $");
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Income percent: " + incomePercent + "%");
        System.out.println("====================================");
    }

    @Override
    public void withdraw(Scanner sc) {
        System.out.println("Enter amount of withdraw: ");
        int quantity = sc.nextInt();
        withdrawMoney(quantity);
    }

    @Override
    public boolean withdrawMoney(int quantity) {
        if (quantity <= 0) {
            System.out.println("Amount must be positive!");
            return false;
        }
        if (quantity > quantityOfMoney) {
            System.out.println("Operation canceled! Not enough money");
            return false;
        }
        quantityOfMoney -= quantity;
        addTransaction("WITHDRAW", quantity);
        System.out.println("Success! You withdrew " + quantity + " $");
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

    //incoming
    public void addInterest() {
        int income = (int) (quantityOfMoney * incomePercent / 100);
        quantityOfMoney += income;
        addTransaction("INTEREST", income);
        System.out.println("Interest added: " + income + " $");
    }
}
