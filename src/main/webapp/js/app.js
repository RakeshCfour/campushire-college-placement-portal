// CampusHire Frontend JavaScript - REST API Integration

async function fetchJSON(url, options = {}) {
    try {
        const response = await fetch(url, options);
        if (response.status === 401) {
            localStorage.removeItem('loggedIn');
            if(!window.location.pathname.endsWith('login.html') && !window.location.pathname.endsWith('index.html')) {
                window.location.href = 'login.html';
            }
            return null;
        }
        return await response.json();
    } catch (e) {
        console.error("Fetch error:", e);
        return null;
    }
}

async function checkAuth() {
    const res = await fetchJSON('/api/auth/me');
    if (res && res.success) {
        localStorage.setItem("loggedIn", "true");
        return res.user;
    } else {
        localStorage.removeItem("loggedIn");
        return null;
    }
}

function validateEmail(email) { return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email); }

document.getElementById("loginForm")?.addEventListener("submit", async e => {
    e.preventDefault();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const msg = document.getElementById("loginMsg");
    
    if(!validateEmail(email) || password.length < 4) {
        msg.innerHTML = '<div class="alert alert-danger">Please enter a valid email and password.</div>';
        return;
    }
    
    msg.innerHTML = '<div class="alert alert-info">Logging in...</div>';
    
    const res = await fetchJSON('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });
    
    if (res && res.success) {
        localStorage.setItem("loggedIn", "true");
        if (res.user.role === 'ADMIN') {
            location.href = "admin.html";
        } else {
            location.href = "dashboard.html";
        }
    } else {
        msg.innerHTML = `<div class="alert alert-danger">${res ? res.message : 'Login failed'}</div>`;
    }
});

document.getElementById("registerForm")?.addEventListener("submit", async e => {
    e.preventDefault();
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const branch = document.getElementById("branch").value;
    const cgpa = Number(document.getElementById("cgpa").value);
    const password = document.getElementById("regPassword").value;
    const msg = document.getElementById("regMsg");
    
    if (name.length < 3 || !validateEmail(email) || cgpa < 0 || cgpa > 10 || password.length < 4) {
        msg.innerHTML = '<div class="alert alert-danger">Please enter valid details. CGPA must be between 0 and 10.</div>';
        return;
    }
    
    msg.innerHTML = '<div class="alert alert-info">Registering...</div>';
    
    const res = await fetchJSON('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, branch, cgpa, password })
    });
    
    if (res && res.success) {
        msg.innerHTML = '<div class="alert alert-success">Registration successful! Redirecting to login...</div>';
        setTimeout(() => location.href = "login.html", 1000);
    } else {
        msg.innerHTML = `<div class="alert alert-danger">${res ? res.message : 'Registration failed'}</div>`;
    }
});

async function renderJobs() {
    const box = document.getElementById("jobList"); 
    if(!box) return;
    
    const jobs = await fetchJSON('/api/drives');
    if (!jobs) return;
    
    box.innerHTML = jobs.map(j => `
        <div class="col-md-6 col-lg-4">
            <div class="job-card card h-100 p-4 shadow-sm">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <div class="company-logo bg-primary text-white d-flex align-items-center justify-content-center fw-bold fs-4" style="width:50px; height:50px; border-radius:10px;">${j.company[0]}</div>
                    <span class="badge text-bg-${j.status === 'OPEN' ? 'success' : 'secondary'} border">${j.status}</span>
                </div>
                <h4 class="fw-bold mb-1">${j.role}</h4>
                <div class="text-secondary mb-3">${j.company}</div>
                <div class="job-meta d-flex gap-2 mb-3 flex-wrap">
                    <span class="badge bg-light text-dark border"><i class="bi bi-mortarboard me-1"></i>CGPA ${j.minCgpa}+</span>
                    <span class="badge bg-light text-dark border"><i class="bi bi-geo-alt me-1"></i>${j.location}</span>
                    <span class="badge bg-light text-dark border"><i class="bi bi-briefcase me-1"></i>${j.type}</span>
                </div>
                <p class="text-muted small mb-4 flex-grow-1">${j.description}</p>
                <button class="btn btn-dark w-100 mt-auto" onclick="applyJob(${j.id}, ${j.minCgpa})">Apply Now</button>
            </div>
        </div>
    `).join("");
}

async function applyJob(driveId, minCgpa) {
    const user = await checkAuth();
    if (!user) {
        alert("Please register/login first.");
        location.href = "login.html";
        return;
    }
    
    if (user.cgpa < minCgpa) {
        alert(`You are not eligible. Minimum CGPA for this drive is ${minCgpa}. Your CGPA is ${user.cgpa}.`);
        return;
    }
    
    const res = await fetchJSON('/api/applications/apply', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ driveId })
    });
    
    if (res && res.success) {
        alert("Application submitted successfully!");
    } else {
        alert(res ? res.message : "Failed to apply.");
    }
}

async function renderDashboard() {
    const user = await checkAuth();
    if (!user) {
        location.href = "login.html";
        return;
    }
    
    document.getElementById("welcome").textContent = `Welcome, ${user.name.split(" ")[0]}.`;
    document.getElementById("dashName").textContent = user.name;
    document.getElementById("dashEmail").textContent = user.email;
    document.getElementById("dashBranch").textContent = user.branch || 'N/A';
    document.getElementById("dashCgpa").textContent = user.cgpa ? Number(user.cgpa).toFixed(2) : 'N/A';
    document.getElementById("avatar").textContent = user.name[0].toUpperCase();
    
    const apps = await fetchJSON('/api/applications/mine') || [];
    const jobs = await fetchJSON('/api/drives') || [];
    
    document.getElementById("appCount").textContent = apps.length;
    document.getElementById("shortCount").textContent = apps.filter(a => a.status === 'SHORTLISTED' || a.status === 'SELECTED').length;
    document.getElementById("driveCount").textContent = jobs.length;
    
    const box = document.getElementById("applications");
    box.innerHTML = apps.length ? apps.map(a => `
        <div class="application-row d-flex justify-content-between align-items-center p-3 border-bottom">
            <div>
                <b class="d-block text-dark">${a.role}</b>
                <div class="text-secondary small">${a.company} &bull; Applied on ${a.appliedOn}</div>
            </div>
            <span class="badge ${a.status==='APPLIED' ? 'text-bg-primary' : (a.status==='SELECTED' ? 'text-bg-success' : (a.status==='REJECTED' ? 'text-bg-danger' : 'text-bg-warning'))} rounded-pill px-3 py-2">${a.status}</span>
        </div>
    `).join("") : '<div class="p-4 text-center text-secondary">No applications yet. Explore the placement drives to get started.</div>';
}

async function logout() {
    await fetchJSON('/api/auth/logout', { method: 'POST' });
    localStorage.removeItem("loggedIn");
    location.href = "index.html";
}

// Auto-init pages
if(document.getElementById("jobList")) renderJobs();
if(document.getElementById("dashName")) renderDashboard();

// Basic admin routing logic protection
if(window.location.pathname.endsWith('admin.html')) {
    checkAuth().then(user => {
        if(!user || user.role !== 'ADMIN') {
            location.href = "dashboard.html";
        } else {
            renderAdminDashboard();
        }
    });
}
