package dao;

import models.Transaction;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Save transaction
    public boolean saveTransaction(Transaction transaction) {

        String sql = "INSERT INTO transactions " +"(transaction_id, account_id, transaction_type, amount, target_account_id) " +"VALUES (?, ?, ?, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, transaction.getTransactionId());
            ps.setInt(2, transaction.getAccountId());
            ps.setString(3, transaction.getTransactionType());
            ps.setDouble(4, transaction.getAmount());
            ps.setInt(5, transaction.getTargetAccountId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Transaction Save Error");
            e.printStackTrace();
        }

        return false;
    }

    // Get transaction history
    public List<Transaction> getTransactionHistoryByAccountId(int accountId) {

        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions " +"WHERE account_id = ? " +"ORDER BY transaction_date DESC";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Transaction transaction = new Transaction();

                transaction.setTransactionId(rs.getString("transaction_id"));

                transaction.setAccountId(rs.getInt("account_id"));

                int target =rs.getInt("target_account_id");

                if (!rs.wasNull()) {
                    transaction.setTargetAccountId(target);
                }

                transaction.setTransactionType(rs.getString("transaction_type"));

                transaction.setAmount(rs.getDouble("amount"));

                transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());

                list.add(transaction);
            }

        } catch (Exception e) {
            System.out.println("Transaction History Error");
            e.printStackTrace();
        }

        return list;
    }
}