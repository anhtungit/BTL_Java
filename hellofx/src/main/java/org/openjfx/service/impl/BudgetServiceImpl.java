package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.BudgetRecord;
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
    public List<BudgetRecord> getIncomeOutcome(LocalDate from, LocalDate to) {
        String sql = "SELECT AccountID, date, SUM(Income) AS Income, SUM(Outcome) AS Outcome FROM (" +

                " SELECT acc.AccountID, DATE(i.CreatedAt) AS date, IF(i.Status = 1, i.TotalAmount, 0) AS Income, 0 AS Outcome " +
                " FROM Invoice i " +
                " JOIN BookingDetail bd ON i.InvoiceID = bd.InvoiceID " +
                " JOIN Employee em ON bd.EmployeeID = em.EmployeeID " +
                " JOIN Account acc ON em.AccountID = acc.AccountID " +
                " WHERE i.CreatedAt BETWEEN ? AND ? " +

                " UNION ALL " +

                " SELECT ex.AccountID, ex.ExpenseDate AS date, 0 AS Income, ex.Amount AS Outcome " +
                " FROM Expense ex " +
                " WHERE ex.ExpenseDate BETWEEN ? AND ? " +

                ") x GROUP BY AccountID, date ORDER BY date";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        if (from == null || to == null) {
            from = LocalDate.of(1970, 1, 1); 
            to = LocalDate.now();
        }

        stmt.setDate(1, java.sql.Date.valueOf(from));
        stmt.setDate(2, java.sql.Date.valueOf(to));
        stmt.setDate(3, java.sql.Date.valueOf(from));
        stmt.setDate(4, java.sql.Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            List<BudgetRecord> buds = new ArrayList<>();
            while (rs.next()) {

                BudgetRecord bud = new BudgetRecord();
                bud.setAccountID(rs.getInt("AccountID"));
                bud.setDate(rs.getDate("date").toLocalDate());
                bud.setIncome(rs.getInt("Income"));
                bud.setOutcome(rs.getInt("Outcome"));
                buds.add(bud);
            }
            return buds;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}