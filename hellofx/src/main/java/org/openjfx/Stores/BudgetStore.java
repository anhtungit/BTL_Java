package org.openjfx.Stores;

import org.openjfx.Models.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetStore {
    private static final List<Transaction> transactions = new ArrayList<>();

    static {
        transactions.add(new Transaction("2025-10-01", "5000000", "0"));
        transactions.add(new Transaction("2025-10-03", "0", "1500000"));
        transactions.add(new Transaction("2025-10-10", "7000000", "2000000"));
    }

    public static List<Transaction> getAll() {
        return new ArrayList<>(transactions);
    }

    public static List<Transaction> filterByDate(LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> {
                    LocalDate d = LocalDate.parse(t.getDate());
                    boolean afterFrom = (from == null || !d.isBefore(from));
                    boolean beforeTo = (to == null || !d.isAfter(to));
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());
    }

    public static void add(Transaction t) {
        transactions.add(t);
    }
}
