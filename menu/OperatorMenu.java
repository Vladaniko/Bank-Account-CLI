package menu;

import account.AccountComparators;
import exception.AccountNotFoundException;
import service.BankService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class OperatorMenu {
    private OperatorMenu() {}

    //======================operator menu==============================

    public static void open(Scanner sc, BankService service) {
        boolean running = true;

        while (running) {
            System.out.println("\n===Operator Menu===");
            System.out.println("[1] Show all accounts");
            System.out.println("[2] Sort accounts");
            System.out.println("[3] Show accounts by type");
            System.out.println("[4] Filter by minimal balance");
            System.out.println("[5] Find account by number");
            System.out.println("[6] Show pending requests");
            System.out.println("[7] Process next request");
            System.out.println("[8] Process all requests");
            System.out.println("[9] Update account owner");
            System.out.println("[10] Change account number");
            System.out.println("[11] Delete account");
            System.out.println("[12] Bank report");
            System.out.println("[0] Back");
            System.out.print("Enter choice: ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        // natural order, Comparable
                        service.printAccounts(service.getAccountsNatural());
                        break;

                    case 2:
                        sortMenu(sc, service);
                        break;

                    case 3:
                        showByType(sc, service);
                        break;

                    case 4:
                        System.out.print("Minimal balance: ");
                        int min = sc.nextInt();
                        service.printAccounts(service.filterByMinBalance(min));
                        break;

                    case 5:
                        System.out.print("Account number: ");
                        service.getAccount(sc.nextInt()).displayInfo();
                        break;

                    case 6:
                        service.printPendingRequests();
                        break;

                    case 7:
                        service.processNextRequest();
                        break;

                    case 8:
                        service.processAllRequests();
                        break;

                    case 9:
                        updateOwner(sc, service);
                        break;

                    case 10:
                        System.out.print("Old account number: ");
                        int oldNumber = sc.nextInt();
                        System.out.print("New account number: ");
                        int newNumber = sc.nextInt();
                        service.changeAccountNumber(oldNumber, newNumber);
                        break;

                    case 11:
                        System.out.print("Account number to delete: ");
                        service.removeAccount(sc.nextInt());
                        break;

                    case 12:
                        service.printReport();
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

    // dynamic sorting with different comparators
    private static void sortMenu(Scanner sc, BankService service) {
        System.out.println("[1] By balance (richest first)");
        System.out.println("[2] By surname and name");
        System.out.println("[3] By type and balance");
        System.out.print("Your choice: ");
        int choice = sc.nextInt();

        if (choice == 1) {
            service.printAccounts(service.getSortedAccounts(AccountComparators.BY_BALANCE_DESC));
        } else if (choice == 2) {
            service.printAccounts(service.getSortedAccounts(AccountComparators.BY_SURNAME_NAME));
        } else if (choice == 3) {
            service.printAccounts(service.getSortedAccounts(AccountComparators.BY_TYPE_THEN_BALANCE));
        } else {
            System.out.println("Unknown menu item!");
        }
    }

    private static void showByType(Scanner sc, BankService service) {
        System.out.println("[1] Credit");
        System.out.println("[2] Debit");
        System.out.println("[3] Business");
        System.out.println("[4] Savings");
        System.out.print("Your choice: ");
        int choice = sc.nextInt();

        String type;
        if (choice == 1) {
            type = "CreditAccount";
        } else if (choice == 2) {
            type = "DebitAccount";
        } else if (choice == 3) {
            type = "BusinessAccount";
        } else if (choice == 4) {
            type = "SavingsAccount";
        } else {
            System.out.println("Unknown menu item!");
            return;
        }
        service.printAccounts(service.getByType(type));
    }

    private static void updateOwner(Scanner sc, BankService service) {
        System.out.print("Account number: ");
        int number = sc.nextInt();
        // check that account exists before asking other data
        service.getAccount(number);

        System.out.print("New name: ");
        String name = sc.next();
        System.out.print("New surname: ");
        String surname = sc.next();
        System.out.print("New phone number: ");
        int phone = sc.nextInt();

        service.updateOwner(number, name, surname, phone);
    }
}
