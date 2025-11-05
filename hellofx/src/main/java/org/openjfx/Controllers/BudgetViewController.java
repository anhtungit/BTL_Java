package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.openjfx.entity.Expense;
import org.openjfx.service.BudgetService;
import org.openjfx.service.impl.BudgetServiceImpl;

import java.time.LocalDate;
import java.util.List;

public class BudgetViewController {

    @FXML 
    private DatePicker dateFrom;
    @FXML 
    private DatePicker dateTo;
    @FXML
    private TableView<Expense> tableExpenses;
    @FXML
    private TableColumn<Expense, Integer> colAmount;
    @FXML
    private TableColumn<Expense, Integer> colAccountID;
    @FXML
    private TableColumn<Expense, LocalDate> colDate;
    @FXML 
    private Label lblTotalIncome;
    @FXML 
    private Label lblTotalExpense;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();

    BudgetService budgetService = new BudgetServiceImpl();

    @FXML
    public void initialize() {


        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAccountID.setCellValueFactory(new PropertyValueFactory<>("accountID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));

        tableExpenses.setItems(expenses);

        loadAll();
    }

    private void loadAll() {
        List<Expense> data = budgetService.getAllExpenses();
        expenses.setAll(data);
        updateTotals(data);
    }

    @FXML
    private void handleFilter() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        List<Expense> filtered = budgetService.filterExpensesByDate(from, to);
        expenses.setAll(filtered);
        updateTotals(filtered);
    }

    private void updateTotals(List<Expense> data) {
        // double totalIncome = data.stream().mapToDouble(t -> t.getIncomeValue()).sum();
        double totalExpense = data.stream().mapToDouble(t -> t.getAmount()).sum();

        // lblTotalIncome.setText(String.format("%,.0f", totalIncome));
        lblTotalExpense.setText(String.format("%,.0f", totalExpense));
    }
}
