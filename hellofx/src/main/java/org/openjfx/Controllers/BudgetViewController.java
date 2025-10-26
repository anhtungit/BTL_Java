package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.openjfx.Models.Transaction;
import org.openjfx.Stores.BudgetStore;

import java.time.LocalDate;
import java.util.List;

public class BudgetViewController {

    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private TableView<Transaction> tableTransactions;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colIncome;
    @FXML private TableColumn<Transaction, String> colExpense;
    @FXML private Label lblTotalIncome;
    @FXML private Label lblTotalExpense;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(cell -> cell.getValue().dateProperty());
        colIncome.setCellValueFactory(cell -> cell.getValue().incomeProperty());
        colExpense.setCellValueFactory(cell -> cell.getValue().expenseProperty());

        tableTransactions.setItems(transactions);

        loadAll();
    }

    private void loadAll() {
        List<Transaction> data = BudgetStore.getAll();
        transactions.setAll(data);
        updateTotals(data);
    }

    @FXML
    private void handleFilter() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        List<Transaction> filtered = BudgetStore.filterByDate(from, to);
        transactions.setAll(filtered);
        updateTotals(filtered);
    }

    private void updateTotals(List<Transaction> list) {
        double totalIncome = list.stream().mapToDouble(t -> t.getIncomeValue()).sum();
        double totalExpense = list.stream().mapToDouble(t -> t.getExpenseValue()).sum();

        lblTotalIncome.setText(String.format("%,.0f", totalIncome));
        lblTotalExpense.setText(String.format("%,.0f", totalExpense));
    }
}
