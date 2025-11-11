package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.openjfx.entity.BudgetView;
import org.openjfx.entity.Employee;
import org.openjfx.entity.Expense;
import org.openjfx.entity.InventoryItem;
import org.openjfx.entity.Invoice;
import org.openjfx.service.BudgetService;
import org.openjfx.service.impl.BudgetServiceImpl;
import org.openjfx.service.impl.InvoiceServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BudgetViewController {

    @FXML 
    private DatePicker dateFrom;
    @FXML 
    private DatePicker dateTo;
    @FXML
    private TableView<BudgetView> tableExpenses;
    @FXML
    private TableColumn<BudgetView, Integer> colOutcome;
    @FXML
    private TableColumn<BudgetView, Integer> colIncome;
    @FXML
    private TableColumn<BudgetView, Integer> colAccountID;
    @FXML
    private TableColumn<BudgetView, LocalDate> colDate;
    @FXML 
    private Label lblTotalIncome;
    @FXML 
    private Label lblTotalExpense;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private ObservableList<BudgetView> budgetViews = FXCollections.observableArrayList();


    BudgetService budgetService = new BudgetServiceImpl();

    @FXML
    public void initialize() {

        colAccountID.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("thu"));
        colOutcome.setCellValueFactory(new PropertyValueFactory<>("chi"));

        // colIncome.setCellValueFactory(new PropertyValueFactory<>("incomeValue"));
        // colOutcome.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // colAccountID.setCellValueFactory(new PropertyValueFactory<>("accountID"));
        // colDate.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));

        tableExpenses.setItems(budgetViews);

        //loadAll();
    }

    private void loadAll() {
        // List<Expense> data = budgetService.getAllExpenses();
        // // int totalIncome = InvoiceServiceImpl.getTotalAmountByDate();
        // expenses.setAll(data);
        // updateTotals(data);
    }

    @FXML
    private void handleFilter() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        ObservableList<BudgetView> filtered = BudgetService.loadBudgetData(from, to);
        budgetViews.setAll(filtered);
        updateTotals(filtered);
    }

    private void updateTotals(List<BudgetView> data) {

        // double totalIncome = data.stream().mapToDouble(t -> t.getIncomeValue()).sum();
        // double totalExpense = data.stream().mapToDouble(t -> t.getAmount()).sum();

        // lblTotalIncome.setText(String.format("%,.0f", totalIncome));
        // lblTotalExpense.setText(String.format("%,.0f", totalExpense));
    }
}
