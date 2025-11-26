package org.openjfx.Controllers;

import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;
import org.openjfx.service.impl.MenuItemServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuItemSearchController {

    @FXML
    private TextField txtSearchKeyword;
    @FXML
    private TableView<MenuItem> tableResult;
    @FXML
    private TableColumn<MenuItem, String> colItemName;
    @FXML
    private TableColumn<MenuItem, String> colCurrentPrice;

    private ObservableList<MenuItem> allItems;
    private ObservableList<MenuItem> filteredItems;

    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    public void initialize() {
        // Load dữ liệu từ database
        // menuItemService.loadFromDatabase();

   colItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        colCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));

        allItems = FXCollections.observableArrayList(menuItemService.getAllMenuItem());
        filteredItems = FXCollections.observableArrayList(allItems);

        tableResult.setItems(filteredItems);

        txtSearchKeyword.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredTable(newValue);
        });

        Tooltip tip = new Tooltip(
            "Cú pháp tìm kiếm: \n"
            + "- Gõ tên món (không dấu và có dấu) \n"
            + "- Gõ số: Tìm giá chính xác (vd: 30000) \n"
            + "- Khoảng giá: 10000-30000\n"
            + "- So sánh: >/</>=/<= + {giá tiền} (vd: >=20000, <=45000, >1000)"
        );
        txtSearchKeyword.setTooltip(tip);
    }

    //lọc mà không cần enter
    public void filteredTable(String keyword){
        if(keyword == null || keyword.isBlank()){
            filteredItems.setAll(allItems);
            return;
        }

        String kw = keyword.trim().toLowerCase();
        String noAccentKw = removeAccent(kw);//ham lay ra tu khong dau, cà phê -> ca phe

        filteredItems.setAll(allItems.filtered(item -> {
            String name = item.getItemName().toLowerCase();
            String noAccentName = removeAccent(name);

            int price = item.getCurrentPrice();

            //Tim theo TEN
            if(name.contains(kw)) return true;

            if(noAccentName.contains(noAccentKw)) return true;

            //Tim kiem theo gia
            if(kw.matches("\\d+")){
                int val = Integer.parseInt(kw);
                if(price == val) return true;
            }

            //Tim kiem theo khoang gia VD 10000-30000
            if(kw.contains("-")){
                try{
                    String[] ps = kw.split("-");
                    int minVal = Integer.parseInt(ps[0].trim());
                    int maxVal = Integer.parseInt(ps[1].trim());
                    if(price >= minVal && price<=maxVal) return true;
                } catch(Exception ignored){
                }
            }

            //Tim kiem theo so sanh gia VD >20000, >=10000, <=45000
            try {
                if(kw.startsWith(">=")){
                    int v = Integer.parseInt(kw.substring(2).trim());
                    if(price >= v) return true;
                }
                if(kw.startsWith("<=")){
                    int v = Integer.parseInt(kw.substring(2).trim());
                    if(price <= v) return true;
                }
                if(kw.startsWith(">")){
                    int v = Integer.parseInt(kw.substring(1).trim());
                    if(price > v) return true;
                }
                if(kw.startsWith("<")){
                    int v = Integer.parseInt(kw.substring(1).trim());
                    if(price < v) return true;
                }
            } catch (Exception ignored) {
            }
            return false;
        }));
    }

    public static String removeAccent(String input){
        if(input == null) return "";
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearchKeyword.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập từ khóa để tìm kiếm!");
            return;
        }

        filteredItems.setAll(allItems.filtered(item -> item.getItemName().toLowerCase().contains(keyword)));

        if (filteredItems.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Kết quả", "Không tìm thấy món nào phù hợp.");
        }
    }

    @FXML
    private void handleRefresh() {
        txtSearchKeyword.clear();
        filteredItems.setAll(allItems);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
