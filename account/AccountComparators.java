package account;

import java.util.Comparator;

public class AccountComparators {
    private AccountComparators() {}

    // richest first
    public static final Comparator<Account> BY_BALANCE_DESC =
            (a, b) -> Integer.compare(b.getQuantityOfMoney(), a.getQuantityOfMoney());

    // alphabetical: surname, then name
    public static final Comparator<Account> BY_SURNAME_NAME = (a, b) -> {
        int result = a.getSurname().compareToIgnoreCase(b.getSurname());
        if (result != 0) {
            return result;
        }
        return a.getName().compareToIgnoreCase(b.getName());
    };

    // multi-criteria: account type, then balance (bigger first), then number
    public static final Comparator<Account> BY_TYPE_THEN_BALANCE = (a, b) -> {
        int result = a.getType().compareTo(b.getType());
        if (result != 0) {
            return result;
        }
        result = Integer.compare(b.getQuantityOfMoney(), a.getQuantityOfMoney());
        if (result != 0) {
            return result;
        }
        return a.compareTo(b);
    };
}
