package com.campushire.dao;

import com.campushire.model.Application;
import com.campushire.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDao {

    public boolean apply(int driveId, int userId) {
        String sql = "INSERT INTO applications (drive_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, driveId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Application> getApplicationsByUser(int userId) {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, d.company, d.role FROM applications a JOIN drives d ON a.drive_id = d.id WHERE a.user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application();
                    app.setId(rs.getInt("id"));
                    app.setDriveId(rs.getInt("drive_id"));
                    app.setUserId(rs.getInt("user_id"));
                    app.setStatus(rs.getString("status"));
                    app.setAppliedOn(rs.getString("applied_on"));
                    app.setCompany(rs.getString("company"));
                    app.setRole(rs.getString("role"));
                    apps.add(app);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public List<Application> getAllApplications() {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, d.company, d.role, u.name as studentName, u.email as studentEmail, u.cgpa " +
                     "FROM applications a " +
                     "JOIN drives d ON a.drive_id = d.id " +
                     "JOIN users u ON a.user_id = u.id";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setDriveId(rs.getInt("drive_id"));
                app.setUserId(rs.getInt("user_id"));
                app.setStatus(rs.getString("status"));
                app.setAppliedOn(rs.getString("applied_on"));
                app.setCompany(rs.getString("company"));
                app.setRole(rs.getString("role"));
                app.setStudentName(rs.getString("studentName"));
                app.setStudentEmail(rs.getString("studentEmail"));
                app.setCgpa(rs.getDouble("cgpa"));
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public boolean updateStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
