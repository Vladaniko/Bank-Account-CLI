package tariff;

import java.util.Scanner;

public class CardTariff {
    private String cardType;      
    private String creditType;    
    private int creditLimit;
    private double percent;       // credit: debt percent, savings: income percent
    private int monthlyFee;
    private String company;       
    private double taxPercent;    

    // getters
    public String getCardType() { return cardType; }
    public String getCreditType() { return creditType; }
    public int getCreditLimit() { return creditLimit; }
    public double getPercent() { return percent; }
    public int getMonthlyFee() { return monthlyFee; }
    public String getCompany() { return company; }
    public double getTaxPercent() { return taxPercent; }

    public void chooseTariff(Scanner sc) {
        while (true) {
            System.out.println("\n===Choose card===");
            System.out.println("[1] Credit");
            System.out.println("[2] Debit");
            System.out.println("[3] Savings");
            System.out.println("[4] Business account");
            System.out.print("Your choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                cardType = "Credit";
                chooseCreditType(sc);
                break;
            } else if (choice == 2) {
                cardType = "Debit";
                creditType = "-";
                creditLimit = 0;
                percent = 0;
                monthlyFee = 2;
                break;
            } else if (choice == 3) {
                cardType = "Savings";
                creditType = "-";
                creditLimit = 0;
                percent = 5;
                monthlyFee = 0;
                break;
            } else if (choice == 4) {
                cardType = "Business";
                creditType = "-";
                creditLimit = 0;
                percent = 0;
                monthlyFee = 15;
                taxPercent = 2;
                askCompany(sc);
                break;
            } else {
                System.out.println("Unknown card type!");
            }
        }
        displayTariff();
    }

    private void askCompany(Scanner sc) {
        while (true) {
            System.out.println("Enter company name:");
            company = sc.nextLine();
            if (!company.isEmpty()) {
                break;
            }
            System.out.println("Company name can't be empty!");
        }
    }

    private void chooseCreditType(Scanner sc) {
        while (true) {
            System.out.println("\n===Choose credit type===");
            System.out.println("[1] Business");
            System.out.println("[2] Agricultural");
            System.out.print("Your choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                creditType = "Business";
                creditLimit = 5000;
                percent = 3;
                monthlyFee = 10;
                break;
            } else if (choice == 2) {
                creditType = "Agricultural";
                creditLimit = 3000;
                percent = 1.5;
                monthlyFee = 5;
                break;
            } else {
                System.out.println("Unknown credit type!");
            }
        }
    }

    public void displayTariff() {
        System.out.println("====================================");
        System.out.println("[Tariff]");
        System.out.println("Card type: " + cardType);
        if (cardType.equals("Credit")) {
            System.out.println("Credit type: " + creditType);
            System.out.println("Credit limit: " + creditLimit + " $");
            System.out.println("Debt percent: " + percent + "%");
        }
        if (cardType.equals("Savings")) {
            System.out.println("Income percent: " + percent + "%");
        }
        if (cardType.equals("Business")) {
            System.out.println("Company: " + company);
            System.out.println("Tax on deposit: " + taxPercent + "%");
        }
        System.out.println("Monthly fee: " + monthlyFee + " $");
        System.out.println("====================================");
    }
}
