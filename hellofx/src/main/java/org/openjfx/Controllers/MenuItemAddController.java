package org.openjfx.Controllers;

import org.openjfx.Models.Ingredient;
import org.openjfx.entity.MenuItem;
import org.openjfx.service.MenuItemService;
import org.openjfx.service.impl.MenuItemServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;

public class MenuItemAddController extends MenuItemControllerBase {


    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtCurrentPrice;
    @FXML
    private TableView<Ingredient> tableRecipe;
    @FXML
    private TableColumn<Ingredient, String> colInventoryItemName;
    @FXML
    private TableColumn<Ingredient, String> colAmount;
    @FXML
    private TableColumn<Ingredient, String> colUnitName;

    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();


    MenuItemService menuItemService = new MenuItemServiceImpl();

    @FXML
    public void initialize() {
        colInventoryItemName.setCellValueFactory(cell -> cell.getValue().inventoryItemNameProperty());
        colAmount.setCellValueFactory(cell -> cell.getValue().amountProperty());
        colUnitName.setCellValueFactory(cell -> cell.getValue().unitNameProperty());
        tableRecipe.setItems(ingredients);

        tableRecipe.setEditable(true);

        colInventoryItemName.setCellFactory(TextFieldTableCell.forTableColumn());
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
        colUnitName.setCellFactory(TextFieldTableCell.forTableColumn());

        colInventoryItemName.setOnEditCommit(event -> event.getRowValue().setInventoryItemName(event.getNewValue()));
        colAmount.setOnEditCommit(event -> event.getRowValue().setAmount(event.getNewValue()));
        colUnitName.setOnEditCommit(event -> event.getRowValue().setUnitName(event.getNewValue()));

        for (int i = 0; i < 3; i++) {
            ingredients.add(new Ingredient("", "", ""));
        }

        //Chi nhap so va gioi han la 10 ty cho gia tien

        txtCurrentPrice.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            //Neu xoa het thi cho phep
            if(newText.isEmpty()){
                txtCurrentPrice.setStyle("-fx-border-color: red; -fx-border-width: 1.5;");
                txtCurrentPrice.setTooltip(new Tooltip("Giá tiền không được bỏ trống!"));
                return change;
            }
            else{
                txtCurrentPrice.setStyle(null);
                txtCurrentPrice.setTooltip(null);
            }

            //bỏ qua dấu phẩy nếu người dùng nhập
            String cleanText = newText.replaceAll(",", "");

            //Không cho nhập chữ
            if(!cleanText.matches("\\d*")){
                return null;
            }

            try{
                long val = Long.parseLong(cleanText);
                if(val > 10_000_000_000L){
                    return null;
                }
            } catch(NumberFormatException e){
                return null;
            }

            return change;

        }));

        
    }

    @FXML
    private void handleAddMenuItem() {
        String name = txtItemName.getText().trim();
        String priceText = txtCurrentPrice.getText().trim();

        if (name.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên món!");
            return;
        }

        if (priceText.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập giá tiền!");
            return;
        }

        try {
            String cleanPriceText = priceText.replaceAll("[^0-9]", "");
            int price = Integer.parseInt(cleanPriceText);

            if (price <= 0) {
                showAlert("Lỗi", "Gía tiền phải lớn hơn 0!");
                return;
            }

            MenuItem newItem = new MenuItem(name, price);
            menuItemService.addMenuItem(newItem);
            // reloadData();

            // Clear form
            txtItemName.clear();
            txtCurrentPrice.clear();
            ingredients.clear();
            for (int i = 0; i < 3; i++) {
                ingredients.add(new Ingredient("", "", ""));
            }

            showAlert("Thành công!", "Đã thêm món \"" + name + "\" vào thực đơn!");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá tiền không hợp lệ! Vui lòng chỉ nhập số.");
        } catch (RuntimeException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void handleCancel() {
        txtItemName.clear();
        txtCurrentPrice.clear();
        ingredients.clear();
    }
}
