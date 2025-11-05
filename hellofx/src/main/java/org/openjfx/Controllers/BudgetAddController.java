package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import org.openjfx.App;
import org.openjfx.Models.Expense;

import org.openjfx.entity.Employee;
import org.openjfx.service.BudgetService;
import org.openjfx.service.impl.BudgetServiceImpl;


import java.time.LocalDate;

public class BudgetAddController {

    @FXML
    private TableView<Expense> tableExpenses;
    @FXML
    private TableColumn<Expense, Integer> colAmount;
    @FXML
    private TableColumn<Expense, String> colDescription;
    @FXML
    private TableColumn<Expense, LocalDate> colDate;
    @FXML
    private Button btnSave;
    @FXML
private Button btnCancel;

    BudgetService budgetService = new BudgetServiceImpl();

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();
    Employee employee = App.getEmployeeLogin();

    @FXML
    public void initialize() {
        tableExpenses.setEditable(true);
        int defaultAccountID = employee.getAccountID();

        // Gán dữ liệu cho từng cột
        colAmount.setCellValueFactory(cell -> cell.getValue().amountProperty().asObject());
        colDescription.setCellValueFactory(cell -> cell.getValue().expenseDescriptionProperty());
        colDate.setCellValueFactory(cell -> cell.getValue().expenseDateProperty());

        // Cho phép chỉnh sửa các cột
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        colDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        // Khi người dùng edit xong thì cập nhật vào model
        colAmount.setOnEditCommit(e -> {
            e.getRowValue().setAmount(e.getNewValue());
            tableExpenses.refresh();
        });

        colDescription.setOnEditCommit(e -> {
            e.getRowValue().setExpenseDescription(e.getNewValue());
            tableExpenses.refresh();
        });

        colDate.setOnEditCommit(e -> {
            e.getRowValue().setExpenseDate(e.getNewValue());
            tableExpenses.refresh();
        });

        // Thêm 1 dòng mặc định để nhập
        expenses.add(new Expense(0, defaultAccountID, 0, "", LocalDate.now()));
        tableExpenses.setItems(expenses);
    }

    @FXML
    private void handleSave() {
        for (Expense e : expenses) {
            if (e.getExpenseDescription() != null && !e.getExpenseDescription().isEmpty()) {
               
                budgetService.addExpense(new Expense(
                        e.getExpenseID(),
                        e.getAccountID(),
                        e.getAmount(),
                        e.getExpenseDescription(),
                        e.getExpenseDate()
                ));
            }
        }

        showAlert("Thành công", "Đã lưu các khoản chi tiêu mới!");
        expenses.clear();
        expenses.add(new Expense(0, 0, 0, "", LocalDate.now()));
    }

    @FXML
    private void handleCancel() {
        expenses.clear();
        expenses.add(new Expense(0, 0, 0, "", LocalDate.now()));
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
