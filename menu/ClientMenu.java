package menu;

import account.Account;
import account.CreditAccount;
import account.SavingsAccount;
import exception.AccountNotFoundException;
import service.BankService;
import tariff.CardTariff;
import transaction.RequestType;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientMenu {
    private ClientMenu() {}

    //===============client menu===================================

    public static void open(Scanner sc, Account acc, CardTariff tariff, BankService service) {
        boolean running = true;

        while (running) {
            System.out.println("\n===Client Menu (" + acc.getName() + " " + acc.getSurname() + ")===");
            System.out.println("[1] Display info");
            System.out.println("[2] Deposit");
            System.out.println("[3] Withdraw");
            if (acc instanceof CreditAccount) {
                System.out.println("[4] Pay off loan");
            }
            if (acc instanceof SavingsAccount) {
                System.out.println("[4] Add interest");
            }
            System.out.println("[5] Show tariff");
            System.out.println("[6] Send request to operator");
            System.out.println("[7] Show history");
            System.out.println("[0] Back");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        acc.displayInfo();
                        break;

                    case 2:
                        acc.deposit(sc);
                        break;

                    case 3:
                        acc.withdraw(sc);
                        break;

                    case 4:
                        if (acc instanceof CreditAccount) {
                            ((CreditAccount) acc).payOffLoan(sc);
                        } else if (acc instanceof SavingsAccount) {
                            ((SavingsAccount) acc).addInterest();
                        } else {
                            System.out.println("Unknown menu item!");
                        }
                        break;

                    case 5:
                        tariff.displayTariff();
                        break;

                    case 6:
                        sendRequest(sc, acc, service);
                        break;

                    case 7:
                        acc.displayHistory();
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println("Unknown menu item!");
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Invalid input! Enter a number.");
            } catch (IllegalArgumentException | AccountNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    //============puts requests to queue=======================
    private static void sendRequest(Scanner sc, Account acc, BankService service) {
        System.out.println("[1] Deposit request");
        System.out.println("[2] Withdraw request");
        System.out.print("Your choice: ");
        int type = sc.nextInt();

        if (type != 1 && type != 2) {
            System.out.println("Unknown request type!");
            return;
        }

        System.out.print("Enter amount: ");
        int amount = sc.nextInt();

        RequestType requestType = (type == 1) ? RequestType.DEPOSIT : RequestType.WITHDRAW;
        service.submitRequest(acc.getAccountNumber(), requestType, amount);
    }
}
