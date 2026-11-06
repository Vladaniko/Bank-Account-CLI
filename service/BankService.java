package service;

import account.Account;
import account.AccountComparators;
import account.BusinessAccount;
import exception.AccountNotFoundException;
import transaction.RequestType;
import transaction.TransactionRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class BankService {
    private final List<Account> accounts = new ArrayList<>();
    private final Map<Integer, Account> accountsByNumber = new HashMap<>();
    private final Map<String, List<Account>> accountsByType = new TreeMap<>();

    // phone numbers must be unique
    private final Set<Integer> usedPhones = new HashSet<>();

    //requests from clients, higher priority first, then FIFO
    private final Queue<TransactionRequest> requestQueue = new PriorityQueue<>();

    //requests for report
    private final List<TransactionRequest> processedRequests = new ArrayList<>();

    //===============CRUD======================

    //add account to list and to all indexes
    public void addAccount(Account acc) {
        if (acc == null) {
            throw new IllegalArgumentException("Account can't be null!");
        }
        if (accountsByNumber.containsKey(acc.getAccountNumber())) {
            throw new IllegalArgumentException("Account " + acc.getAccountNumber() + " already exists!");
        }
        if (!usedPhones.add(acc.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number " + acc.getPhoneNumber() + " is already used!");
        }
        accounts.add(acc);
        accountsByNumber.put(acc.getAccountNumber(), acc);
        accountsByType.computeIfAbsent(acc.getType(), k -> new ArrayList<>()).add(acc);
    }

    //search by account number
    public Account getAccount(int accountNumber) {
        Account acc = accountsByNumber.get(accountNumber);
        if (acc == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return acc;
    }

    public boolean exists(int accountNumber) {
        return accountsByNumber.containsKey(accountNumber);
    }

    public boolean isPhoneUsed(int phone) {
        return usedPhones.contains(phone);
    }

    //update owner data, phone set must stay in sync
    public void updateOwner(int accountNumber, String name, String surname, int phone) {
        Account acc = getAccount(accountNumber);
        if (name.isEmpty() || surname.isEmpty()) {
            throw new IllegalArgumentException("Name and surname can't be empty!");
        }
        if (phone != acc.getPhoneNumber()) {
            if (!usedPhones.add(phone)) {
                throw new IllegalArgumentException("Phone number " + phone + " is already used!");
            }
            usedPhones.remove(acc.getPhoneNumber());
            acc.setPhoneNumber(phone);
        }
        acc.setName(name.trim());
        acc.setSurname(surname.trim());
        System.out.println("Account " + accountNumber + " updated.");
    }

    //change key of account: index map and pending requests must be updated too
    public void changeAccountNumber(int oldNumber, int newNumber) {
        Account acc = getAccount(oldNumber);
        if (newNumber <= 0) {
            throw new IllegalArgumentException("ID can't be negative!");
        }
        if (accountsByNumber.containsKey(newNumber)) {
            throw new IllegalArgumentException("Account " + newNumber + " already exists!");
        }
        accountsByNumber.remove(oldNumber);
        acc.setAccountNumber(newNumber);
        accountsByNumber.put(newNumber, acc);

        for (TransactionRequest req : requestQueue) {
            if (req.getAccountNumber() == oldNumber) {
                req.setAccountNumber(newNumber);
            }
        }
        System.out.println("Account number changed: " + oldNumber + " on " + newNumber);
    }

    public Account removeAccount(int accountNumber) {
        Account acc = getAccount(accountNumber);

        accounts.remove(acc);
        accountsByNumber.remove(accountNumber);

        List<Account> group = accountsByType.get(acc.getType());
        if (group != null) {
            group.remove(acc);
            if (group.isEmpty()) {
                accountsByType.remove(acc.getType());
            }        requestQueue.removeIf(req -> req.getAccountNumber() == accountNumber);

        }
        usedPhones.remove(acc.getPhoneNumber());

        int before = requestQueue.size();
        requestQueue.removeIf(req -> req.getAccountNumber() == accountNumber);
        int removedRequests = before - requestQueue.size();

        System.out.println("Account " + accountNumber + " deleted. Canceled requests: " + removedRequests);
        return acc;
    }

    //random free number
    public int generateAccountNumber() {
        int number;
        do {
            number = (int) (Math.random() * 9000) + 1000;
        } while (accountsByNumber.containsKey(number));
        return number;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // natural order (Comparable)
    public List<Account> getAccountsNatural() {
        List<Account> copy = new ArrayList<>(accounts);
        java.util.Collections.sort(copy);
        return copy;
    }

    // dynamic order (Comparator)
    public List<Account> getSortedAccounts(Comparator<Account> comparator) {
        List<Account> copy = new ArrayList<>(accounts);
        copy.sort(comparator);
        return copy;
    }

    public List<Account> getByType(String type) {
        List<Account> group = accountsByType.get(type);
        if (group == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(group);
    }

    public List<Account> filterByMinBalance(int minBalance) {
        List<Account> result = new ArrayList<>();
        for (Account acc : accounts) {
            if (acc.getQuantityOfMoney() >= minBalance) {
                result.add(acc);
            }
        }
        return result;
    }

    public void printAccounts(List<Account> list) {
        System.out.println("====================================");
        if (list.isEmpty()) {
            System.out.println("No accounts found!");
        }
        for (Account acc : list) {
            System.out.println(acc);
        }
        System.out.println("====================================");
    }

    // client puts request to queue, business accounts have bigger priority
    public TransactionRequest submitRequest(int accountNumber, RequestType type, int amount) {
        Account acc = getAccount(accountNumber);
        if (type == null) {
            throw new IllegalArgumentException("Request type can't be null!");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }
        int priority = (acc instanceof BusinessAccount) ? 2 : 1;
        TransactionRequest req = new TransactionRequest(accountNumber, type, amount, priority);
        requestQueue.offer(req);
        System.out.println("Request #" + req.getId() + " accepted. Requests in queue: " + requestQueue.size());
        return req;
    }

    // operator takes the next request with poll()
    public boolean processNextRequest() {
        TransactionRequest req = requestQueue.poll();
        if (req == null) {
            System.out.println("Queue is empty!");
            return false;
        }
        System.out.println("Processing: " + req);

        Account acc = accountsByNumber.get(req.getAccountNumber());
        boolean success = false;
        if (acc == null) {
            System.out.println("Account not found!");
        } else if (req.getType() == RequestType.DEPOSIT) {
            success = acc.depositMoney(req.getAmount());
        } else {
            success = acc.withdrawMoney(req.getAmount());
        }

        req.setStatus(success ? "DONE" : "REJECTED");
        processedRequests.add(req);
        return success;
    }

    public void processAllRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty!");
            return;
        }
        while (!requestQueue.isEmpty()) {
            processNextRequest();
        }
    }

    public int getPendingCount() {
        return requestQueue.size();
    }

    // iterator of PriorityQueue is not ordered, so sort a copy
    public void printPendingRequests() {
        System.out.println("====================================");
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty!");
        }
        List<TransactionRequest> list = new ArrayList<>(requestQueue);
        java.util.Collections.sort(list);
        for (TransactionRequest req : list) {
            System.out.println(req);
        }
        System.out.println("====================================");
    }

    //===================analitics======================

    public int getTotalMoney() {
        int total = 0;
        for (Account acc : accounts) {
            total += acc.getQuantityOfMoney();
        }
        return total;
    }

    public void printReport() {
        System.out.println("====================================");
        System.out.println("[Bank report]");
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet!");
            System.out.println("====================================");
            return;
        }

        int total = getTotalMoney();
        System.out.println("Accounts: " + accounts.size());
        System.out.println("Total money: " + total + " $");
        System.out.println("Average balance: " + (total / accounts.size()) + " $");

        Account richest = getSortedAccounts(AccountComparators.BY_BALANCE_DESC).get(0);
        System.out.println("Richest client: " + richest.getName() + " " + richest.getSurname()
                + " (" + richest.getQuantityOfMoney() + " $)");

        System.out.println("=====By type=====");
        for (Map.Entry<String, List<Account>> entry : accountsByType.entrySet()) {
            int sum = 0;
            for (Account acc : entry.getValue()) {
                sum += acc.getQuantityOfMoney();
            }
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " acc., " + sum + " $");
        }

        int done = 0;
        int rejected = 0;
        for (TransactionRequest req : processedRequests) {
            if (req.getStatus().equals("DONE")) {
                done++;
            } else {
                rejected++;
            }
        }
        System.out.println("=====Requests=====");
        System.out.println("Pending: " + requestQueue.size());
        System.out.println("Done: " + done);
        System.out.println("Rejected: " + rejected);
        System.out.println("====================================");
    }
}
