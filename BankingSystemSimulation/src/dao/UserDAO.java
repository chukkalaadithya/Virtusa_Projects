package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import models.User;
import util.DBConnection;

public class UserDAO {

    public User login(String username, String password) throws Exception {
        String sql ="SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }

        return null;
    }

    public boolean registerUser(User user) throws Exception {

        String sql ="INSERT INTO users(full_name, username, password, mobile_number, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getMobileNumber());
            ps.setString(5, user.getAddress());

            return ps.executeUpdate() > 0;
        }
    }

    public User getUserByUsername(String username) throws Exception {

        String sql ="SELECT * FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }

        return null;
    }

    public boolean updateUser(User user)throws Exception {

        String sql ="UPDATE users SET full_name = ?, mobile_number = ?, address = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getMobileNumber());
            ps.setString(3, user.getAddress());
            ps.setInt(4, user.getUserId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(int userId)throws Exception {

        String sql ="DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private User mapUser(ResultSet rs) throws Exception {

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