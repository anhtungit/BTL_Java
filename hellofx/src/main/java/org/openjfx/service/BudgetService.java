package org.openjfx.service;

import java.time.LocalDate;
import java.util.List;

import org.openjfx.entity.BudgetRecord;
import org.openjfx.entity.Expense;

import javafx.collections.ObservableList;


public interface BudgetService {
    List<Expense> getAllExpenses();
    void addExpense(Expense item);
    List<BudgetRecord> getIncomeOutcome(LocalDate from, LocalDate to);
}
