package runner;

import account.Account;
import account.BusinessAccount;
import account.CreditAccount;
import account.DebitAccount;
import account.SavingsAccount;
import menu.ClientMenu;
import menu.OperatorMenu;
import registration.Registration;
import service.BankService;
import tariff.CardTariff;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        //bank with all accounts inside
        BankService service = new BankService();
        fillDemoData(service);

        //login or registration
        Registration reg = new Registration();
        if (!reg.Menu(sc, service)) {
            System.out.println("Access denied!");
            return;
        }

        //card choice
        CardTariff tariff = new CardTariff();
        tariff.chooseTariff(sc);

        //create account 
        int phone = Integer.parseInt(reg.getPhoneNumber());
        if (service.isPhoneUsed(phone)) {
            System.out.println("Phone number " + phone + " is already used!");
            return;
        }

        int accNumber = service.generateAccountNumber();
        Account acc;

        if (tariff.getCardType().equals("Credit")) {
            acc = new CreditAccount(reg.getName(), reg.getSurname(), accNumber, 0, phone,
                    tariff.getCreditLimit(), tariff.getPercent());
        } else if (tariff.getCardType().equals("Debit")) {
            acc = new DebitAccount(reg.getName(), reg.getSurname(), accNumber, 0, phone,
                    tariff.getMonthlyFee());
        } else if (tariff.getCardType().equals("Business")) {
            acc = new BusinessAccount(reg.getName(), reg.getSurname(), accNumber, 0, phone,
                    tariff.getCompany(), tariff.getTaxPercent());
        } else {
            acc = new SavingsAccount(reg.getName(), reg.getSurname(), accNumber, 0, phone,
                    tariff.getPercent());
        }

        try {
            service.addAccount(acc);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("\nAccount created! Your account number: " + accNumber);

        //choose role
        boolean running = true;

        while (running) {
            System.out.println("\n===Choose role===");
            System.out.println("[1] Client");
            System.out.println("[2] Operator");
            System.out.println("[0] Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        ClientMenu.open(sc, acc, tariff, service);
                        break;

                    case 2:
                        OperatorMenu.open(sc, service);
                        break;

                    case 0:
                        running = false;
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Unknown menu item!");
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Invalid input! Enter a number.");
            }
        }
    }

    //demo data
    private static void fillDemoData(BankService service) {
        service.addAccount(new DebitAccount("Nurzhigit", "Arapbaev", 1001, 500, 700123456, 2));
        service.addAccount(new SavingsAccount("Denis", "Tronin", 1002, 1500, 555987654, 5));
        service.addAccount(new BusinessAccount("Sulaiman", "Kadyrov", 1003, 3000, 777111222, "E. Corp.", 2));
        service.addAccount(new CreditAccount("Baiel", "Aibekov", 1004, 200, 500333444, 5000, 3));
    }
}
