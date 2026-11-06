package account;

import java.util.Scanner;

public class BusinessAccount extends Account{
    protected String company;
    protected double taxPercent;

    public BusinessAccount(String name, String surname, int accountNumber, int quantityOfMoney, int phoneNumber, String company, double taxPercent) {
        super(name, surname, accountNumber, quantityOfMoney, phoneNumber);
        this.company = company;
        this.taxPercent = taxPercent;
    }

    public String getCompany() { return company; }
    public double getTaxPercent() { return taxPercent; }
    public void setCompany(String company) { this.company = company; }
    public void setTaxPercent(double taxPercent) { this.taxPercent = taxPercent; }

    @Override
    public void displayInfo() {
        System.out.println("====================================");
        System.out.println("[Business Account]");
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Account number: " + accountNumber);
        System.out.println("Quantity of money: " + quantityOfMoney + " $");
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Company: " + company);
        System.out.println("Tax percent: " + taxPercent + "%");
        System.out.println("====================================");
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

    
    // deposit with tax 
    @Override
    public boolean depositMoney(int sum) {
        if (sum <= 0) {
            System.out.println("Amount must be positive!");
            return false;
        }
        int tax = (int) (sum * taxPercent / 100);
        quantityOfMoney += sum - tax;
        addTransaction("DEPOSIT", sum - tax);
        System.out.println("Money was successfully loaded! Tax " + taxPercent + "% = " + tax + " $");
        return true;
    }
    
    @Override
    public void deposit(Scanner sc) {
        System.out.println("Enter money for deposit: ");
        int sum = sc.nextInt();
        depositMoney(sum);
    }
    
    @Override
    public void withdraw(Scanner sc) {
        System.out.println("Enter amount of withdraw: ");
        int quantity = sc.nextInt();
        withdrawMoney(quantity);
    }
}
