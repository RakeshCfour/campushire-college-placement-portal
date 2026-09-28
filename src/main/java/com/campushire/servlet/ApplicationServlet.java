package com.campushire.servlet;

import com.campushire.dao.ApplicationDao;
import com.campushire.model.Application;
import com.campushire.model.User;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/applications/*")
public class ApplicationServlet extends HttpServlet {
    private ApplicationDao appDao = new ApplicationDao();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(401);
            out.print("{\"success\":false, \"message\":\"Unauthorized\"}");
            return;
        }
        User user = (User) session.getAttribute("user");
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/mine".equals(pathInfo)) {
            List<Application> apps = appDao.getApplicationsByUser(user.getId());
            out.print(gson.toJson(apps));
        } else if ("/all".equals(pathInfo) && "ADMIN".equals(user.getRole())) {
            List<Application> apps = appDao.getAllApplications();
            out.print(gson.toJson(apps));
        } else {
            resp.setStatus(403);
            out.print("{\"success\":false, \"message\":\"Forbidden\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(401);
            out.print("{\"success\":false, \"message\":\"Unauthorized\"}");
            return;
        }
        User user = (User) session.getAttribute("user");

        String pathInfo = req.getPathInfo();
        if ("/apply".equals(pathInfo)) {
            try {
                Map requestData = gson.fromJson(req.getReader(), Map.class);
                int driveId = ((Double) requestData.get("driveId")).intValue();
                boolean success = appDao.apply(driveId, user.getId());
                if (success) {
                    out.print("{\"success\":true}");
                } else {
                    out.print("{\"success\":false, \"message\":\"You have already applied or an error occurred.\"}");
                }
            } catch (Exception e) {
                out.print("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
            }
        } else if ("/updateStatus".equals(pathInfo) && "ADMIN".equals(user.getRole())) {
            try {
                Map requestData = gson.fromJson(req.getReader(), Map.class);
                int appId = ((Double) requestData.get("applicationId")).intValue();
                String status = (String) requestData.get("status");
                boolean success = appDao.updateStatus(appId, status);
                out.print("{\"success\":" + success + "}");
            } catch (Exception e) {
                out.print("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
            }
        }
    }
}
