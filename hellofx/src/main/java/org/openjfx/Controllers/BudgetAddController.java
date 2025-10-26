package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.openjfx.Models.Transaction;
import org.openjfx.Stores.BudgetStore;

import java.time.LocalDate;

public class BudgetAddController {

    @FXML private TableView<Transaction> tableExpenses;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private TableColumn<Transaction, String> colAmount;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private ObservableList<Transaction> expenses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tableExpenses.setEditable(true);

        colDate.setCellValueFactory(cell -> cell.getValue().dateProperty());
        colDescription.setCellValueFactory(cell -> cell.getValue().incomeProperty()); // tạm dùng incomeProperty cho mô tả
        colAmount.setCellValueFactory(cell -> cell.getValue().expenseProperty());

        // Cho phép nhập text
        colDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn());

        // Bắt sự kiện chỉnh sửa
        colDate.setOnEditCommit(e -> e.getRowValue().dateProperty().set(e.getNewValue()));
        colDescription.setOnEditCommit(e -> e.getRowValue().incomeProperty().set(e.getNewValue()));
        colAmount.setOnEditCommit(e -> e.getRowValue().expenseProperty().set(e.getNewValue()));

        // Thêm một dòng trống mặc định để nhập
        expenses.add(new Transaction(LocalDate.now().toString(), "", "0"));
        tableExpenses.setItems(expenses);
    }

    @FXML
    private void handleSave() {
        for (Transaction t : expenses) {
            if (!t.getExpense().equals("0") && !t.getExpense().isEmpty()) {
                BudgetStore.add(new Transaction(t.getDate(), "0", t.getExpense()));
            }
        }

        showAlert("Thành công", "Đã lưu các khoản chi tiêu mới!");
        expenses.clear();
        expenses.add(new Transaction(LocalDate.now().toString(), "", "0"));
    }

    @FXML
    private void handleCancel() {
        expenses.clear();
        expenses.add(new Transaction(LocalDate.now().toString(), "", "0"));
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
