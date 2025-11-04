package org.openjfx.service;

import java.util.List;

import org.openjfx.Models.Expense;

public interface BudgetService {
    List<Expense> getAllExpenses();
    void addExpense(Expense item);
}
