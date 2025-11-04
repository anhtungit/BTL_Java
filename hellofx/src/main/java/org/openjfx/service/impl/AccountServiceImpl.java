package org.openjfx.service.impl;

import org.openjfx.DB.DBConnection;
import org.openjfx.entity.Account;
import org.openjfx.entity.Employee;
import org.openjfx.service.AccountService;

import java.sql.*;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
    @Override
    public void delete(int accountID) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          DELETE FROM Account
                          WHERE AccountID = ?;
                          """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountID);
            ps.executeUpdate();
            System.out.println("Deleted employee id: " + accountID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int create(Account account) {
        int generatedKey = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                          INSERT INTO Account(UserName, Password)
                          VALUES (?, ?)
                          """;
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedKey = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedKey;
    }

    @Override
    public boolean userNameIsPresent(String userName) {
        Account account = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT *
                    FROM Account
                    WHERE UserName = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
