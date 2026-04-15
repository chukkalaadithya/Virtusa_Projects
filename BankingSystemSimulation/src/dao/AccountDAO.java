package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Account;
import util.DBConnection;

public class AccountDAO {

    public boolean createAccount(Account account)throws Exception {
        String sql ="INSERT INTO accounts(user_id, account_type, balance) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setInt(1, account.getUserId());
            ps.setString(2, account.getAccountType());
            ps.setDouble(3, account.getBalance());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Account> getAccountsByUserId(int userId)throws Exception {
        List<Account> accounts =new ArrayList<>();
        String sql ="SELECT * FROM accounts WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    accounts.add(mapAccount(rs));
                }
            }
        }

        return accounts;
    }

    public Account getAccountById(int accountId)throws Exception {
        String sql ="SELECT * FROM accounts WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        }

        return null;
    }

    public Account getAccountByUserIdAccountType(int userId,String accountType)throws Exception {

        String sql ="SELECT * FROM accounts WHERE user_id = ? AND account_type = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, accountType);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        }

        return null;
    }

    public boolean updateBalance(int accountId,double balance) throws Exception {

        String sql ="UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setDouble(1, balance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    public double getBalance(int userId,int accountId) throws Exception {

        String sql ="SELECT balance FROM accounts WHERE user_id = ? AND account_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, accountId);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }

        return 0;
    }

    public boolean deleteAccountsByUserId(int userId)throws Exception {
        String sql ="DELETE FROM accounts WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Account mapAccount(ResultSet rs)throws Exception {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}