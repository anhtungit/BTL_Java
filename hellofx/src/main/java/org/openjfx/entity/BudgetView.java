package org.openjfx.entity;

import java.time.LocalDate;

public class BudgetView {
    private int accountId;
    private LocalDate date;
    private int thu;
    private int chi;

    public BudgetView(int accountId, LocalDate date, int thu, int chi) {
        this.accountId = accountId;
        this.date = date;
        this.thu = thu;
        this.chi = chi;
    }

    public int getAccountId() {
        return accountId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getThu() {
        return thu;
    }

    public int getChi() {
        return chi;
    }
}
