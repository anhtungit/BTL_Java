package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.Models.Expense;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.service.BudgetService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BudgetServiceImpl implements BudgetService {

    @Override
    public List<Expense> getAllExpenses() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expense");
             ResultSet rs = stmt.executeQuery()) {

            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                Expense expense = new Expense();
                expense.setExpenseID(rs.getInt("ExpenseID"));
                expense.setAccountID(rs.getInt("AccountID"));
                expense.setExpenseDescription(rs.getString("ExpenseDescription"));
                expense.setExpenseDate(rs.getDate("ExpenseDate").toLocalDate());
                expenses.add(expense);
            }
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void addExpense(Expense item) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Expense (AccountID, ExpenseDescription, ExpenseDate) VALUES (?, ?, ?)")) {

            stmt.setInt(1, item.getAccountID());
            stmt.setString(2, item.getExpenseDescription());
            stmt.setDate(3, java.sql.Date.valueOf(item.getExpenseDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}