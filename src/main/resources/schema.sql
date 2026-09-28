CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'STUDENT',
    branch TEXT,
    cgpa REAL
);

CREATE TABLE IF NOT EXISTS drives (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    company TEXT NOT NULL,
    role TEXT NOT NULL,
    min_cgpa REAL,
    type TEXT,
    location TEXT,
    description TEXT,
    status TEXT DEFAULT 'OPEN'
);

CREATE TABLE IF NOT EXISTS applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    drive_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    status TEXT DEFAULT 'APPLIED',
    applied_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(drive_id) REFERENCES drives(id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    UNIQUE(drive_id, user_id)
);

-- Insert an admin user for testing
INSERT OR IGNORE INTO users (id, name, email, password, role) VALUES (1, 'Admin User', 'admin@campushire.com', 'admin123', 'ADMIN');

-- Insert some dummy drives
INSERT OR IGNORE INTO drives (id, company, role, min_cgpa, type, location, description) VALUES 
(1, 'TCS', 'Software Developer', 7.0, 'Full Time', 'Hyderabad', 'Graduate software development role involving application development, testing and support.'),
(2, 'Infosys', 'Systems Engineer', 6.5, 'Full Time', 'Pune', 'Entry-level technology role for graduates interested in software and enterprise systems.'),
(3, 'Accenture', 'Associate Software Engineer', 7.5, 'Full Time', 'Bengaluru', 'Technology role covering application development, testing and problem solving.');
