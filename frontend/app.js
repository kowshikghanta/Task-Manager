const BASE_URL = 'http://localhost:9090/api';

// State Management
let currentToken = localStorage.getItem('orbit_token');
let currentUserId = localStorage.getItem('orbit_userId');
let userName = localStorage.getItem('orbit_userName');
let currentPage = 0;
let currentStatusFilter = '';
let editingTaskId = null;
const PAGE_SIZE = 5;

// DOM Elements
const authContainer = document.getElementById('auth-container');
const dashboardContainer = document.getElementById('dashboard-container');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const authError = document.getElementById('auth-error');
const taskList = document.getElementById('task-list');
const pageDisplay = document.getElementById('page-display');
const btnPrev = document.getElementById('prev-page');
const btnNext = document.getElementById('next-page');

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (currentToken && currentUserId) {
        showDashboard();
    }
});

// UI Toggles
function toggleAuth() {
    authError.innerText = '';
    if (loginForm.style.display === 'none') {
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
    } else {
        loginForm.style.display = 'none';
        registerForm.style.display = 'block';
    }
}

function showDashboard() {
    authContainer.style.display = 'none';
    dashboardContainer.style.display = 'flex';
    document.getElementById('welcome-message').innerText = `Hello ${userName || 'User'}`;
    loadTasks(0, currentStatusFilter);
}

function logout() {
    localStorage.removeItem('orbit_token');
    localStorage.removeItem('orbit_userId');
    localStorage.removeItem('orbit_userName');
    currentToken = null;
    currentUserId = null;
    
    dashboardContainer.style.display = 'none';
    authContainer.style.display = 'block';
    authError.innerText = '';
}

// Authentication
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const res = await fetch(`${BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        if (!res.ok) throw new Error('Invalid credentials');
        
        const data = await res.json();
        const jwtPayload = parseJwt(data.token);
        
        // Let's fetch all users, find ours, and get the ID.
        const usersRes = await fetch(`${BASE_URL}/users`, {
            headers: { 'Authorization': `Bearer ${data.token}` }
        });
        
        if (!usersRes.ok) throw new Error('Failed to retrieve user profile.');
        
        const users = await usersRes.json();
        const me = users.find(u => u.email === email);
        
        currentToken = data.token;
        currentUserId = me ? me.id : 1; // Fallback
        userName = me ? me.name : "User";
        
        localStorage.setItem('orbit_token', currentToken);
        localStorage.setItem('orbit_userId', currentUserId);
        localStorage.setItem('orbit_userName', userName);
        
        showDashboard();
    } catch (err) {
        authError.style.color = 'var(--danger)';
        authError.innerText = err.message;
    }
});

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    
    try {
        const res = await fetch(`${BASE_URL}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });
        
        if (!res.ok) throw new Error('Registration failed. Email might exist.');
        
        toggleAuth(); // Go to login
        document.getElementById('login-email').value = email;
        authError.innerText = 'Registration successful! Please log in.';
        authError.style.color = 'var(--success)';
    } catch (err) {
        authError.innerText = err.message;
        authError.style.color = 'var(--danger)';
    }
});

// Tasks HTTP Wrapper
async function fetchApi(endpoint, method = 'GET', body = null) {
    const headers = {
        'Authorization': `Bearer ${currentToken}`,
        'Content-Type': 'application/json'
    };
    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);
    
    const res = await fetch(`${BASE_URL}/users/${currentUserId}${endpoint}`, options);
    if (!res.ok && res.status !== 204) {
        if(res.status === 401 || res.status === 403) logout();
        throw new Error('API Request Failed');
    }
    
    if (res.status === 204) return null;
    return res.json();
}

// Tasks Logic
async function loadTasks(pageToLoad = 0, statusFilter = '') {
    currentPage = pageToLoad;
    currentStatusFilter = statusFilter;
    
    // Update filter tabs UI
    if (typeof event !== 'undefined' && event && event.target && event.target.classList && event.target.classList.contains('filter-btn')) {
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
        event.target.classList.add('active');
    }
    
    const sortDir = document.getElementById('sort-select').value;
    let url = `/tasks?page=${currentPage}&size=${PAGE_SIZE}&sortDirection=${sortDir}`;
    if (statusFilter) url += `&status=${statusFilter}`;
    
    try {
        const data = await fetchApi(url);
        renderTasks(data.content);
        
        // Paginator logic
        pageDisplay.innerText = `Page ${data.pageNo + 1} of ${data.totalPages||1}`;
        btnPrev.disabled = data.pageNo === 0;
        btnNext.disabled = data.last;
    } catch (err) {
        console.error(err);
    }
}

function renderTasks(tasks) {
    taskList.innerHTML = '';
    if (tasks.length === 0) {
        taskList.innerHTML = `<p style="text-align:center; margin-top:20px;">No tasks found.</p>`;
        return;
    }
    
    tasks.forEach(task => {
        const li = document.createElement('li');
        li.className = 'task-item';
        
        const isCompleted = task.status === 'COMPLETED';
        const isPending = task.status === 'PENDING';
        
        // Beautiful dropdown inside mapping
        li.innerHTML = `
            <div class="task-content">
                <h3 style="text-decoration: ${isCompleted ? 'line-through' : 'none'}">${task.title}</h3>
                <span class="task-status status-${task.status}">${task.status.replace('_', ' ')}</span>
            </div>
            <div class="task-actions">
                <select class="status-select" onchange="updateTaskStatus(${task.id}, this.value)">
                    <option value="PENDING" ${task.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="IN_PROGRESS" ${task.status === 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                    <option value="COMPLETED" ${task.status === 'COMPLETED' ? 'selected' : ''}>Completed</option>
                </select>
                <button class="edit-btn" onclick="openEditModal(${task.id}, '${task.title.replace(/'/g, "\\'")}', '${(task.description||'').replace(/'/g, "\\'")}')">Edit</button>
                <button class="delete-btn" onclick="deleteTask(${task.id})">Del</button>
            </div>
        `;
        taskList.appendChild(li);
    });
}

async function createTask() {
    const titleInput = document.getElementById('new-task-title');
    const title = titleInput.value.trim();
    if (!title) {
        alert("Please enter a task title before submitting.");
        return;
    }
    
    try {
        await fetchApi('/tasks', 'POST', { title, description: '' });
        titleInput.value = '';
        loadTasks(0, currentStatusFilter);
    } catch(err) {
        console.error(err);
        alert("Failed to create task! Error: " + err.message);
    }
}

async function updateTaskStatus(taskId, newStatus) {
    try {
        await fetchApi(`/tasks/${taskId}/status`, 'PATCH', { status: newStatus });
        loadTasks(currentPage, currentStatusFilter); // Reload current view
    } catch(err) {
        console.error(err);
        alert("Failed to update status.");
    }
}

async function deleteTask(taskId) {
    if (!confirm('Are you sure you want to delete this task?')) return;
    try {
        await fetchApi(`/tasks/${taskId}`, 'DELETE');
        loadTasks(currentPage, currentStatusFilter);
    } catch(err) {
        console.error(err);
        alert("Failed to delete task.");
    }
}

function changePage(delta) {
    loadTasks(currentPage + delta, currentStatusFilter);
}

// Edit Modal Logic
function openEditModal(id, title, desc) {
    editingTaskId = id;
    document.getElementById('edit-task-title').value = title;
    document.getElementById('edit-task-desc').value = desc;
    document.getElementById('edit-modal').style.display = 'flex';
}

function closeEditModal() {
    editingTaskId = null;
    document.getElementById('edit-modal').style.display = 'none';
}

async function submitEditTask() {
    if(!editingTaskId) return;
    const title = document.getElementById('edit-task-title').value.trim();
    const description = document.getElementById('edit-task-desc').value.trim();
    if(!title) {
        alert("Title cannot be blank.");
        return;
    }
    
    try {
        await fetchApi(`/tasks/${editingTaskId}`, 'PUT', { title, description });
        closeEditModal();
        loadTasks(currentPage, currentStatusFilter);
    } catch(err) {
        console.error(err);
        alert("Failed to edit task! Ensure you have correct permissions.");
    }
}

// -----------------------------------------
// DevOps Sidebar & Feature Logic
// -----------------------------------------
function switchView(viewName) {
    // Nav styles
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById('nav-' + viewName).classList.add('active');
    
    // View toggling
    document.querySelectorAll('.section-view').forEach(view => view.classList.remove('active'));
    document.getElementById('view-' + viewName).classList.add('active');
    
    if (viewName === 'metrics') {
        loadMetrics();
    }
}

async function loadMetrics() {
    const container = document.getElementById('metrics-container');
    container.innerHTML = '<p>Loading analytics...</p>';
    
    try {
        const metrics = await fetchApi('/tasks/metrics', 'GET');
        container.innerHTML = `
            <div class="metric-card">
                <h3>Total Network Tasks</h3>
                <div class="big-number">${metrics.totalTasks}</div>
            </div>
            <div class="metric-card">
                <h3>Completed</h3>
                <div class="big-number" style="background: linear-gradient(135deg, #86efac, #4ade80); -webkit-background-clip: text;">${metrics.completedTasks}</div>
            </div>
            <div class="metric-card">
                <h3>In Progress</h3>
                <div class="big-number" style="background: linear-gradient(135deg, #fde047, #facc15); -webkit-background-clip: text;">${metrics.inProgressTasks}</div>
            </div>
            <div class="metric-card">
                <h3>Pending</h3>
                <div class="big-number" style="background: linear-gradient(135deg, #fca5a5, #f87171); -webkit-background-clip: text;">${metrics.pendingTasks}</div>
            </div>
            <div class="metric-card">
                <h3>Active Users</h3>
                <div class="big-number">${metrics.activeUsers}</div>
            </div>
        `;
    } catch (err) {
        console.error(err);
        container.innerHTML = '<p style="color:var(--danger)">Failed to load metrics. Require Administrator privileges.</p>';
    }
}

// Password Form Logic
document.getElementById('password-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const oldPassword = document.getElementById('old-password').value;
    const newPassword = document.getElementById('new-password').value;
    
    // We override the fetchApi URL here slightly since it hits the user root
    try {
        const res = await fetch(`${BASE_URL}/users/${currentUserId}/password`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${currentToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ currentPassword: oldPassword, newPassword: newPassword })
        });
        
        if (!res.ok) throw new Error('Password mismatch or Server Logic failed.');
        
        alert("Password updated successfully!");
        document.getElementById('old-password').value = '';
        document.getElementById('new-password').value = '';
    } catch(err) {
        console.error(err);
        alert(err.message);
    }
});
