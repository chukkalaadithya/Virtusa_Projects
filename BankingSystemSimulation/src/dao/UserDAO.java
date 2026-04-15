package dao;

import models.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // Register User
    public boolean registerUser(User user) throws Exception {  //include
        String sql = "INSERT INTO users(full_name, username, password, mobile_number, address) VALUES(?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getMobileNumber());
            ps.setString(5, user.getAddress());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Registration failed! Something went Wrong");
        }
    }

    
    public User login(String username, String password) throws Exception {  //include
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (Exception e) {
        	e.printStackTrace();
        	throw new Exception("Login failed !",e);
        }

        return null;
    }

    // Update full user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name=?, username=?, password=?, mobile_number=?, address=? WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getMobileNumber());
            ps.setString(5, user.getAddress());
            ps.setInt(6, user.getUserId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete user
    public boolean deleteUser(int userId) {
        String sqlusers = "DELETE FROM users WHERE user_id=?";
        String sqlaccounts="delete from accounts where user_id=?";
        String sqltransactions="DELETE t FROM transactions t " +"JOIN accounts a ON t.account_id = a.account_id " +"WHERE a.user_id = ?";;

        try {
        	Connection con = DBConnection.getConnection();
            PreparedStatement ps1 = con.prepareStatement(sqlusers);
            PreparedStatement ps2=con.prepareStatement(sqlaccounts);
            PreparedStatement ps3=con.prepareStatement(sqltransactions);
            
            ps3.setInt(1, userId);
            ps3.executeUpdate();
            
            ps2.setInt(1, userId);
            ps2.executeUpdate();
            
            ps1.setInt(1,userId);
            return ps1.executeUpdate()>0;    

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get user by ID
    public User getUserByUserId(int userId) {
        String sql = "SELECT * FROM users WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserByUsername(String username) throws Exception {  //include
        String sql = "SELECT * FROM users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to process your request",e);
        }
        return null;
    }

    // Update mobile number
    public boolean updateMobileNumber(int userId, String mobile) {
        String sql = "UPDATE users SET mobile_number=? WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mobile);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update password
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Common mapper method
    private User mapResultSetToUser(ResultSet rs) throws Exception {  //include
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setUserName(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setMobileNumber(rs.getString("mobile_number"));
        user.setAddress(rs.getString("address"));

        return user;
    }
}    