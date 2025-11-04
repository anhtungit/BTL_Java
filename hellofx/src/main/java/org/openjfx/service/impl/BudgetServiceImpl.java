package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.Models.Expense;
import org.openjfx.service.BudgetService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
                expense.setAmount(rs.getInt("Amount"));
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
                     "INSERT INTO Expense (ExpenseID, AccountID, Amount, ExpenseDescription, ExpenseDate) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, item.getExpenseID());
            stmt.setInt(2, item.getAccountID());
            stmt.setInt(3, item.getAmount());
            stmt.setString(4, item.getExpenseDescription());
            stmt.setDate(5, java.sql.Date.valueOf(item.getExpenseDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Expense> filterExpensesByDate(LocalDate from, LocalDate to) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Expense WHERE ExpenseDate BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(from));
            stmt.setDate(2, java.sql.Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                Expense expense = new Expense();
                expense.setExpenseID(rs.getInt("ExpenseID"));
                expense.setAccountID(rs.getInt("AccountID"));
                expense.setAmount(rs.getInt("Amount"));
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
}