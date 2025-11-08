package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.openjfx.entity.BudgetRecord;
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
    private TableView<BudgetRecord> tableExpenses;
    @FXML
    private TableColumn<BudgetRecord, Double> colAmount;
    @FXML
    private TableColumn<BudgetRecord, Double> colIncome;
    @FXML
    private TableColumn<BudgetRecord, Integer> colAccountID;
    @FXML
    private TableColumn<BudgetRecord, LocalDate> colDate;
    @FXML 
    private Label lblTotalIncome;
    @FXML 
    private Label lblTotalExpense;

    private ObservableList<BudgetRecord> records = FXCollections.observableArrayList();
    private BudgetService budgetService = new BudgetServiceImpl();

    @FXML
    public void initialize() {

        colAccountID.setCellValueFactory(new PropertyValueFactory<>("accountID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("outcome"));

        colIncome.setCellFactory(column -> new TableCell<BudgetRecord, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", value)); 
                }
            }
        });

        colAmount.setCellFactory(column -> new TableCell<BudgetRecord, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", value));
                }
            }
        });
        tableExpenses.setItems(records);

        loadAll();
    }

    private void loadAll() {
        List<BudgetRecord> data = budgetService.getIncomeOutcome(null, null);
        records.setAll(data);
        updateTotals(data);
    }

    @FXML
    private void handleFilter() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        List<BudgetRecord> data = budgetService.getIncomeOutcome(from, to);
        records.setAll(data);
        updateTotals(data);
    }

private void updateTotals(List<BudgetRecord> data) {
    double totalIncome = data.stream().mapToDouble(BudgetRecord::getIncome).sum();
    double totalOutcome = data.stream().mapToDouble(BudgetRecord::getOutcome).sum();

    lblTotalIncome.setText(String.format("%,.0f", totalIncome));
    lblTotalExpense.setText(String.format("%,.0f", totalOutcome));
    }
}