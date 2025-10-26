package org.openjfx.Models;

import java.time.LocalDate;

public class ReportItem {
    public enum Type { IMPORT, EXPORT, SALARY, EMPLOYEE, OTHER }

    private LocalDate date;
    private long income;
    private long expense;
    private Type type;

    public ReportItem(LocalDate date, long income, long expense, Type type) {
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.type = type;
    }

    public LocalDate getDate() { return date; }
    public long getIncome() { return income; }
    public long getExpense() { return expense; }
    public Type getType() { return type; }
}
