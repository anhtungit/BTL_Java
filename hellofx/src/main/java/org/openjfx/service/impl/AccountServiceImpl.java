package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.service.AccountService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    @Override
    public Account getAccountByUserName(String userName) {
        Account account = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT AccountID, Password
                    FROM Account
                    WHERE Username = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("AccountID"));
                account.setUsername(userName);
                account.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
        }
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountID) {
        Account account = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT Username, Password
                    FROM Account
                    WHERE AccountID = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setAccountId(accountID);
                account.setUsername(rs.getString("Username"));
                account.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
        }
        return account;
    }

    @Override
    public void save(Account account) {
        try (Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE Employee SET Password = ? WHERE AccountID = ?";
            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, account.getPassword());
            ps.setInt(2, account.getAccountId());
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
