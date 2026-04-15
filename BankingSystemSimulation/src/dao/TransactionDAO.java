package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.Transaction;
import util.DBConnection;

public class TransactionDAO {

    public boolean saveTransaction(Transaction transaction) throws Exception {
        String sql = "INSERT INTO transactions(transaction_id, account_id, transaction_type, amount, target_account_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setString(1,transaction.getTransactionId());
            ps.setInt(2,transaction.getAccountId());
            ps.setString(3,transaction.getTransactionType());
            ps.setDouble(4,transaction.getAmount());
            ps.setInt(5,transaction.getTargetAccountId());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Transaction> getTransactionHistoryByAccountId(int accountId) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions " +"WHERE account_id = ? " + "ORDER BY transaction_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
        	ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(rs.getString("transaction_id"),rs.getInt("account_id"),rs.getInt("target_account_id"),rs.getString("transaction_type"),rs.getDouble("amount"));

                    if (rs.getTimestamp("transaction_date") != null) {
                        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
                    }

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }
    
    public boolean deleteTransactionsByUserId(int userId) throws Exception {
        String sql ="DELETE t FROM transactions t " + "JOIN accounts a ON t.account_id = a.account_id " + "WHERE a.user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        }
    }
}