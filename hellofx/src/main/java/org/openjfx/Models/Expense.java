package org.openjfx.Models;

import java.time.LocalDate;
import javafx.beans.property.*;

public class Expense {

    private final IntegerProperty expenseID;
    private final IntegerProperty accountID;
    private final IntegerProperty Amount;
    private final StringProperty expenseDescription;
    private final ObjectProperty<LocalDate> expenseDate; 

    public Expense() {
        this.expenseID = new SimpleIntegerProperty();
        this.accountID = new SimpleIntegerProperty();
        this.Amount = new SimpleIntegerProperty();
        this.expenseDescription = new SimpleStringProperty();
        this.expenseDate = new SimpleObjectProperty<>(LocalDate.now());
    }

    public Expense(int expenseID, int accountID, int Amount, String expenseDescription, LocalDate expenseDate) {
        this.expenseID = new SimpleIntegerProperty(expenseID);
        this.accountID = new SimpleIntegerProperty(accountID);
        this.Amount = new SimpleIntegerProperty(Amount);
        this.expenseDescription = new SimpleStringProperty(expenseDescription);
        this.expenseDate = new SimpleObjectProperty<>(expenseDate);
    }

    public int getExpenseID() {
        return expenseID.get();
    }

    public void setExpenseID(int expenseID) {
        this.expenseID.set(expenseID);
    }

    public IntegerProperty expenseIDProperty() {
        return expenseID;
    }

    public int getAccountID() {
        return accountID.get();
    }

    public void setAccountID(int accountID) {
        this.accountID.set(accountID);
    }

    public IntegerProperty accountIDProperty() {
        return accountID;
    }

    public String getExpenseDescription() {
        return expenseDescription.get();
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription.set(expenseDescription);
    }

    public StringProperty expenseDescriptionProperty() {
        return expenseDescription;
    }

    public LocalDate getExpenseDate() {
        return expenseDate.get();
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate.set(expenseDate);
    }

    public ObjectProperty<LocalDate> expenseDateProperty() {
        return expenseDate;
    }

    public int getAmount() {
        return Amount.get();
    }
    public void setAmount(int amount) {
        this.Amount.set(amount);
    }
    public IntegerProperty amountProperty() {
        return Amount;
    }
}
