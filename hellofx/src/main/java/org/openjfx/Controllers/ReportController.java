package org.openjfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.openjfx.Models.ReportItem;

import java.time.LocalDate;
import java.util.Locale;

public class ReportController {

    @FXML private CheckBox chkAll, chkImportExport, chkImport, chkSalary, chkEmployee, chkExport, chkOther;
    @FXML private DatePicker dateFrom, dateTo;
    @FXML private Button btnView, btnExport, btnPrint;
    @FXML private TableView<ReportItem> tableReport;
    @FXML private TableColumn<ReportItem, String> colDate;
    @FXML private TableColumn<ReportItem, String> colIncome;
    @FXML private TableColumn<ReportItem, String> colExpense;
    @FXML private Label lblTotalIncome, lblTotalExpense;
    @FXML private ChoiceBox<String> choicePaper;
    @FXML private TextField txtPrinter;
    @FXML private Spinner<Integer> spinnerCopies;

    @FXML
    private void initialize() {
        // Populate choiceBox
        choicePaper.setItems(FXCollections.observableArrayList("A4", "A5", "Letter"));

        // Setup spinner
        spinnerCopies.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Setup table columns
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDate().toString()
        ));
        colIncome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                formatCurrency(c.getValue().getIncome())
        ));
        colExpense.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                formatCurrency(c.getValue().getExpense())
        ));

        // Checkbox "Tất cả"
        chkAll.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                chkImportExport.setSelected(true);
                chkImport.setSelected(true);
                chkExport.setSelected(true);
                chkSalary.setSelected(true);
                chkEmployee.setSelected(true);
                chkOther.setSelected(true);
            } else {
                chkImportExport.setSelected(false);
                chkImport.setSelected(false);
                chkExport.setSelected(false);
                chkSalary.setSelected(false);
                chkEmployee.setSelected(false);
                chkOther.setSelected(false);
            }
        });
    }

    // ================== Nút Xem ==================
    @FXML
    private void handleView() {
        ObservableList<ReportItem> data = filterReportData();
        tableReport.setItems(data);

        long totalIncome = data.stream().mapToLong(ReportItem::getIncome).sum();
        long totalExpense = data.stream().mapToLong(ReportItem::getExpense).sum();

        lblTotalIncome.setText(formatCurrency(totalIncome));
        lblTotalExpense.setText(formatCurrency(totalExpense));
    }

    // ================== Nút Xuất ==================
    @FXML
    private void handleExport() {
        showAlert("Đang phát triển", "Tính năng xuất file đang được phát triển.");
    }

    // ================== Nút In ==================
    @FXML
    private void handlePrint() {
        showAlert("Đang phát triển", "Tính năng in file đang được phát triển.");
    }

    // ================== Hàm filter dữ liệu mock ==================
    private ObservableList<ReportItem> filterReportData() {
        ObservableList<ReportItem> allData = getAllReportData();

        LocalDate from = dateFrom.getValue() != null ? dateFrom.getValue() : LocalDate.MIN;
        LocalDate to = dateTo.getValue() != null ? dateTo.getValue() : LocalDate.MAX;

        return allData.filtered(item -> {
            if (item.getDate().isBefore(from) || item.getDate().isAfter(to)) return false;

            if (chkAll.isSelected()) return true;

            switch (item.getType()) {
                case IMPORT: return chkImport.isSelected() || chkImportExport.isSelected();
                case EXPORT: return chkExport.isSelected() || chkImportExport.isSelected();
                case SALARY: return chkSalary.isSelected();
                case EMPLOYEE: return chkEmployee.isSelected();
                case OTHER: return chkOther.isSelected();
                default: return false;
            }
        });
    }

    // ================== Dữ liệu mock ==================
    private ObservableList<ReportItem> getAllReportData() {
        ObservableList<ReportItem> data = FXCollections.observableArrayList();

        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            data.add(new ReportItem(date, 1000000, 500000, ReportItem.Type.IMPORT));
            data.add(new ReportItem(date, 2000000, 300000, ReportItem.Type.EXPORT));
            data.add(new ReportItem(date, 0, 1500000, ReportItem.Type.SALARY));
            data.add(new ReportItem(date, 0, 200000, ReportItem.Type.OTHER));
        }

        return data;
    }

    // ================== Hàm hỗ trợ ==================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String formatCurrency(long value) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
        return nf.format(value);
    }
}