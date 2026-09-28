package com.campushire.servlet;

import com.campushire.dao.DriveDao;
import com.campushire.model.Drive;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/drives/*")
public class DriveServlet extends HttpServlet {
    private DriveDao driveDao = new DriveDao();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        
        List<Drive> drives = driveDao.getAllDrives();
        out.print(gson.toJson(drives));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // For admin adding drives
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        
        try {
            Drive drive = gson.fromJson(req.getReader(), Drive.class);
            boolean success = driveDao.addDrive(drive);
            if(success) {
                out.print("{\"success\":true}");
            } else {
                out.print("{\"success\":false}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
