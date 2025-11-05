package org.openjfx.entity;

import java.time.LocalDate;

public class Expense {
    private int expenseID;
    private int accountID;
    private int amount;
    private String expenseDescription;
    private LocalDate expenseDate;

    public Expense() {
    }

    public Expense(int expenseID, int accountID, int amount, String expenseDescription, LocalDate expenseDate) {
        this.expenseID = expenseID;
        this.accountID = accountID;
        this.amount = amount;
        this.expenseDescription = expenseDescription;
        this.expenseDate = expenseDate;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
}
