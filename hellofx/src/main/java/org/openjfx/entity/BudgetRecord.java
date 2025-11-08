package org.openjfx.entity;

import java.time.LocalDate;

public class BudgetRecord {
    private int accountID;
    private LocalDate date;
    private double income;
    private double outcome;

    public BudgetRecord(int accountID, LocalDate date, double income, double outcome) {
        this.accountID = accountID;
        this.date = date;
        this.income = income;
        this.outcome = outcome;
    }

    public int getAccountID() {
        return accountID;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getIncome() {
        return income;
    }

    public double getOutcome() {
        return outcome;
    }
}
