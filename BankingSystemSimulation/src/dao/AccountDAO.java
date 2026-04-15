package dao;

import models.Account;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import exceptions.BankingSystemException;

public class AccountDAO {

	public boolean createAccount(Account account) {
	    String sql = "INSERT INTO accounts(user_id, account_type, balance) VALUES (?, ?, ?)";

	    try {
	    	Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setInt(1, account.getUserId());
	        ps.setString(2, account.getAccountType());
	        ps.setDouble(3, account.getBalance());

	        return ps.executeUpdate()>0;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public Account getAccountByUserIdAccountType(int userId,String accountType) {
		String sql="select * from accounts where user_id=? and account_type=?";
		Account acc=null;
		try {
			Connection con=DBConnection.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, accountType);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				acc=mapResultSetToAccount(rs);
				return acc;
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public List<Account> getAccountsByUserId(int userId) {
    	try {
	        String sql = "SELECT * FROM accounts WHERE user_id=?";
	        List<Account> accounts = new ArrayList<>();
	
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            ps.setInt(1, userId);
	
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    accounts.add(mapResultSetToAccount(rs));
	                }
	            }
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	        return accounts;
    	}
    	catch(Exception e) {
    		throw new BankingSystemException("Database error while fetching accounts", e);
    	}
    }

    // Get single account by ID
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public double getBalance(int userId,int accountId) {
    	String sql="select balance from accounts where user_id=? and account_id=?";
    	double balance = 0.00;
    	try {
    		Connection con=DBConnection.getConnection();
    		PreparedStatement ps=con.prepareStatement(sql);
    		ps.setInt(1,userId);
    		ps.setInt(2, accountId);;
    		ResultSet rs=ps.executeQuery();
    		if(rs.next()) {
    			balance=rs.getDouble("balance");
    		}
    	}
    	catch(Exception e) {
    		System.out.println("Error in Getting Balance");
    		e.printStackTrace();
    	}
    	return balance;
    }

    // Update balance
    public boolean updateBalance(int accountId, double balance) {
        String sql = "UPDATE accounts SET balance=? WHERE account_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, balance);
            ps.setInt(2, accountId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete account
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Common mapper method
    private Account mapResultSetToAccount(ResultSet rs) throws Exception {
        Account account = new Account();

        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getDouble("balance"));

        return account;
    }
}