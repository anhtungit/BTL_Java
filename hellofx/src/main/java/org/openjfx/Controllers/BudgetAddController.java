package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import org.openjfx.Models.Expense;
import org.openjfx.Models.Transaction;
import org.openjfx.Stores.BudgetStore;
import org.openjfx.service.BudgetService;
import org.openjfx.service.impl.BudgetServiceImpl;


import java.time.LocalDate;

public class BudgetAddController {

    @FXML
    private TableView<Expense> tableExpenses;
    @FXML
    private TableColumn<Expense, LocalDate> colDate;
    @FXML
    private TableColumn<Expense, String> colDescription;
    @FXML
    private TableColumn<Expense, Integer> colAccountID;
    @FXML
    private Button btnSave;
    @FXML
private Button btnCancel;

    BudgetService budgetService = new BudgetServiceImpl();

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
     public void initialize() {
        tableExpenses.setEditable(true);

        // Gán dữ liệu cho từng cột (binding với property của Expense)
        colDate.setCellValueFactory(cell -> cell.getValue().expenseDateProperty());
        colDescription.setCellValueFactory(cell -> cell.getValue().expenseDescriptionProperty());
        colAccountID.setCellValueFactory(cell -> cell.getValue().accountIDProperty().asObject());

        // Cài cell cho phép chỉnh sửa trực tiếp
        colDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        colAccountID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Xử lý khi người dùng sửa giá trị trực tiếp
        colDate.setOnEditCommit(e -> e.getRowValue().setExpenseDate(e.getNewValue()));
        colDescription.setOnEditCommit(e -> e.getRowValue().setExpenseDescription(e.getNewValue()));
        colAccountID.setOnEditCommit(e -> e.getRowValue().setAccountID(e.getNewValue()));

        expenses.add(new Expense(0, 0, "", LocalDate.now()));
        tableExpenses.setItems(expenses);
    }

    @FXML
    private void handleSave() {
        for (Expense e : expenses) {
            if (e.getExpenseDescription() != null && !e.getExpenseDescription().isEmpty()) {
               
                budgetService.addExpense(new Expense(
                        e.getExpenseID(),
                        e.getAccountID(),
                        e.getExpenseDescription(),
                        e.getExpenseDate()
                ));
            }
        }

        showAlert("Thành công", "Đã lưu các khoản chi tiêu mới!");
        expenses.clear();
        expenses.add(new Expense(0, 0, "", LocalDate.now()));
    }

    @FXML
    private void handleCancel() {
        expenses.clear();
        expenses.add(new Expense(0, 0, "", LocalDate.now()));
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
