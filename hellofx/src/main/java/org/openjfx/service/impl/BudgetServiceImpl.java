package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.BudgetView;
import org.openjfx.entity.Expense;
import org.openjfx.service.BudgetService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
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
    
    public ObservableList<BudgetView> loadBudgetData(LocalDate from, LocalDate to) {
        
        ObservableList<BudgetView> list = FXCollections.observableArrayList();
        String sql = "SELECT AccountID, Ngay, SUM(Thu) AS Thu, SUM(Chi) AS Chi FROM (" +
                "SELECT i.AccountID, i.CreatedAt AS Ngay, i.TotalAmount AS Thu, 0 AS Chi " +
                "FROM Invoice i WHERE i.CreatedAt BETWEEN ? AND ? " +
                "UNION ALL " +
                "SELECT e.AccountID, e.ExpenseDate AS Ngay, 0 AS Thu, e.Amount AS Chi " +
                "FROM Expense e WHERE e.ExpenseDate BETWEEN ? AND ? " +
                ") x GROUP BY AccountID, Ngay ORDER BY Ngay";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            stmt.setDate(3, Date.valueOf(from));
            stmt.setDate(4, Date.valueOf(to));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                list.add(new BudgetView(
                        rs.getInt("AccountID"),
                        rs.getDate("Ngay").toLocalDate(),
                        rs.getInt("Thu"),
                        rs.getInt("Chi")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}