package org.openjfx.service;

import java.time.LocalDate;
import java.util.List;

import org.openjfx.entity.Expense;

public interface BudgetService {
    List<Expense> getAllExpenses();
    void addExpense(Expense item);
    List<Expense> filterExpensesByDate(LocalDate from, LocalDate to);
}
