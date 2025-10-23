package org.openjfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ingredient {
    private final StringProperty tenThanhPhan;
    private final StringProperty khoiLuong;
    private final StringProperty donVi;

    public Ingredient(String ten, String kl, String dv) {
        this.tenThanhPhan = new SimpleStringProperty(ten);
        this.khoiLuong = new SimpleStringProperty(kl);
        this.donVi = new SimpleStringProperty(dv);
    }

    public String getTenThanhPhan() { return tenThanhPhan.get(); }
    public void setTenThanhPhan(String value) { tenThanhPhan.set(value);}
    public StringProperty tenThanhPhanProperty() { return tenThanhPhan; }

    public String getKhoiLuong() { return khoiLuong.get(); }
    public void setKhoiLuong(String value) { khoiLuong.set(value);}
    public StringProperty khoiLuongProperty() { return khoiLuong; }

    public String getDonVi() { return donVi.get(); }
    public void setDonVi(String value) { donVi.set(value);}
    public StringProperty donViProperty() { return donVi; }
}
