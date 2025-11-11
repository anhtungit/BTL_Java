package org.openjfx.service;

import java.time.LocalDate;
import java.util.List;

import org.openjfx.entity.BudgetView;
import org.openjfx.entity.Expense;

import javafx.collections.ObservableList;


public interface BudgetService {
    List<Expense> getAllExpenses();
    void addExpense(Expense item);
    List<Expense> filterExpensesByDate(LocalDate from, LocalDate to);
    static ObservableList<BudgetView> loadBudgetData(LocalDate from, LocalDate to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadBudgetData'");
    }
}
