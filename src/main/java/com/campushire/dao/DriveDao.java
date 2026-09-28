package com.campushire.dao;

import com.campushire.model.Drive;
import com.campushire.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriveDao {

    public List<Drive> getAllDrives() {
        List<Drive> drives = new ArrayList<>();
        String sql = "SELECT * FROM drives";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                drives.add(extractDriveFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drives;
    }

    public Drive getDriveById(int id) {
        String sql = "SELECT * FROM drives WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractDriveFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDrive(Drive drive) {
        String sql = "INSERT INTO drives (company, role, min_cgpa, type, location, description, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drive.getCompany());
            ps.setString(2, drive.getRole());
            ps.setDouble(3, drive.getMinCgpa());
            ps.setString(4, drive.getType());
            ps.setString(5, drive.getLocation());
            ps.setString(6, drive.getDescription());
            ps.setString(7, drive.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Drive extractDriveFromResultSet(ResultSet rs) throws SQLException {
        Drive drive = new Drive();
        drive.setId(rs.getInt("id"));
        drive.setCompany(rs.getString("company"));
        drive.setRole(rs.getString("role"));
        drive.setMinCgpa(rs.getDouble("min_cgpa"));
        drive.setType(rs.getString("type"));
        drive.setLocation(rs.getString("location"));
        drive.setDescription(rs.getString("description"));
        drive.setStatus(rs.getString("status"));
        return drive;
    }
}
