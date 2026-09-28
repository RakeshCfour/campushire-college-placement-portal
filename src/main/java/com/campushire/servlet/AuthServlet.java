package com.campushire.servlet;

import com.campushire.dao.UserDao;
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
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();

        try {
            if ("/login".equals(pathInfo)) {
                User loginReq = gson.fromJson(req.getReader(), User.class);
                User user = userDao.login(loginReq.getEmail(), loginReq.getPassword());
                if (user != null) {
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    result.put("success", true);
                    result.put("user", user);
                } else {
                    result.put("success", false);
                    result.put("message", "Invalid email or password");
                }
            } else if ("/register".equals(pathInfo)) {
                User registerReq = gson.fromJson(req.getReader(), User.class);
                registerReq.setRole("STUDENT");
                boolean success = userDao.registerUser(registerReq);
                result.put("success", success);
                if (!success) result.put("message", "Registration failed. Email might already exist.");
            } else if ("/logout".equals(pathInfo)) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                result.put("success", true);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        out.print(gson.toJson(result));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();

        if ("/me".equals(pathInfo)) {
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                result.put("success", true);
                result.put("user", session.getAttribute("user"));
            } else {
                result.put("success", false);
            }
            out.print(gson.toJson(result));
        }
    }
}
