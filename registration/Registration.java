package registration;

import service.BankService;

import java.util.Scanner;

public class Registration {
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getPhoneNumber() { return phoneNumber; }

    public boolean isPhoneValid(String phone) {
        if (phone.length() != 9) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isPasswordValid(String pass) {
        if (pass.length() < 8) {
            System.out.println("Password must have at least 8 characters!");
            return false;
        }
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetter(c)) {
                hasSymbol = true;
            }
        }

        if (!hasUpper) {
            System.out.println("Password must have at least 1 uppercase letter!");
            return false;
        }
        if (!hasDigit) {
            System.out.println("Password must have at least 1 digit!");
            return false;
        }
        if (!hasSymbol) {
            System.out.println("Password must have at least 1 symbol (!, @, # ...)!");
            return false;
        }
        return true;
    }

    public void register(Scanner sc, BankService service) {
        System.out.println("===Registration===");

        while (true) {
            System.out.println("Enter your name: ");
            name = sc.nextLine().trim();
            if (!name.isEmpty()) {
                break;
            }
            System.out.println("Name can't be empty!");
        }

        while (true) {
            System.out.println("Enter your surname: ");
            surname = sc.nextLine().trim();
            if (!surname.isEmpty()) {
                break;
            }
            System.out.println("Surname can't be empty!");
        }

        while (true) {
            System.out.println("Enter phone number (9 digits):");
            phoneNumber = sc.nextLine().trim();

            if (!isPhoneValid(phoneNumber)) {
                System.out.println("Phone number must have exactly 9 digits!");
                continue;
            }
            int phone = Integer.parseInt(phoneNumber);
            if (service.isPhoneUsed(phone)) {
                System.out.println("Phone number " + phone + " is already used!");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("Create password:");
            password = sc.nextLine();
            if (isPasswordValid(password)) {
                break;
            }
        }
        System.out.println("Registration completed!");
    }

    // Login by phone and password, 3 attempts
    public boolean login(Scanner sc) {
        System.out.println("\n===Login===");

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("Enter phone number:");
            String phone = sc.nextLine().trim();
            System.out.println("Enter password:");
            String pass = sc.nextLine();

            if (phone.equals(phoneNumber) && pass.equals(password)) {
                System.out.println("Welcome, " + name + " " + surname + "!");
                return true;
            }
            System.out.println("Wrong phone or password! Attempts left: " + (3 - attempt));
        }
        System.out.println("Too many attempts!");
        return false;
    }

    // Welcome screen: login or registration
    public boolean Menu(Scanner sc, BankService service) {
        System.out.println("Welcome to ABank!");

        while (true) {
            System.out.println("\n[1] Login");
            System.out.println("[2] Registration");
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                if (phoneNumber == null) {
                    System.out.println("No registered user! Please register first.");
                    continue;
                }
                return login(sc);
            } else if (choice.equals("2")) {
                register(sc, service);
            } else {
                System.out.println("Unknown menu item!");
            }
        }
    }
}
