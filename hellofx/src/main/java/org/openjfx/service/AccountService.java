package org.openjfx.service;

import org.openjfx.entity.Account;

public interface AccountService {
    Account getAccountByUserName(String userName);
    Account getAccountByAccountID(int accountID);
    void save(Account account);
}
