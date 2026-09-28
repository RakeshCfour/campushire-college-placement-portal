package com.campushire.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String branch;
    private double cgpa;

    public User() {}

    public User(int id, String name, String email, String password, String role, String branch, double cgpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.branch = branch;
        this.cgpa = cgpa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
}
