package org.openjfx.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
    private final StringProperty date;
    private final StringProperty income;
    private final StringProperty expense;

    public Transaction(String date, String income, String expense) {
        this.date = new SimpleStringProperty(date);
        this.income = new SimpleStringProperty(income);
        this.expense = new SimpleStringProperty(expense);
    }

    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }

    public String getIncome() { return income.get(); }
    public StringProperty incomeProperty() { return income; }
    public double getIncomeValue() { return parseMoney(income.get()); }

    public String getExpense() { return expense.get(); }
    public StringProperty expenseProperty() { return expense; }
    public double getExpenseValue() { return parseMoney(expense.get()); }

    private double parseMoney(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
