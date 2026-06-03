// Global UI Scripts for Gym Management System with Firebase Integration
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyBPMrRU6bBs8b4usgXHPZ7xY7MC0zVhTps",
  authDomain: "fitness-tracker-a126e.firebaseapp.com",
  projectId: "fitness-tracker-a126e",
  storageBucket: "fitness-tracker-a126e.firebasestorage.app",
  messagingSenderId: "326671169730",
  appId: "1:326671169730:web:35ba17b5b85a0f9358a35b",
  measurementId: "G-BE9D3N8Q0K"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

// Observe Authentication State
onAuthStateChanged(auth, (user) => {
    if (user) {
        localStorage.setItem('isLoggedIn', 'true');
        
        // Update user profile displays across all pages dynamically
        const profileName = document.querySelector('.user-profile-name');
        const profileRole = document.querySelector('.user-profile-role');
        if (profileName) {
            // Display username derived from email or default to "Admin Portal"
            const namePart = user.email.split('@')[0];
            profileName.textContent = namePart.charAt(0).toUpperCase() + namePart.slice(1);
        }
        if (profileRole) {
            profileRole.textContent = "Fitness Manager";
        }
    } else {
        localStorage.removeItem('isLoggedIn');
        window.location.replace('/login');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // 1. Highlight Active Sidebar Link Automatically
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    
    sidebarLinks.forEach(link => {
        const href = link.getAttribute('href');
        // Match base path e.g. /members matched with /members?action=register
        if (currentPath === href || (href !== '/' && href !== '/dashboard' && currentPath.startsWith(href))) {
            sidebarLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        }
    });

    // 2. Inject Logout Button dynamically into Sidebar
    const sidebarMenu = document.querySelector('.sidebar-menu');
    if (sidebarMenu) {
        const logoutLi = document.createElement('li');
        logoutLi.style.marginTop = 'auto'; // Push logout to bottom if flex container
        logoutLi.innerHTML = `
            <a href="#" id="btn-logout" class="sidebar-link" style="color: var(--color-danger); transition: all var(--transition-speed) ease;">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Logout</span>
            </a>
        `;
        sidebarMenu.appendChild(logoutLi);

        const logoutBtn = document.getElementById('btn-logout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                signOut(auth).then(() => {
                    localStorage.removeItem('isLoggedIn');
                    window.location.replace('/login');
                }).catch(err => {
                    console.error("Sign out error:", err);
                });
            });
        }
    }

    // 3. Add subtle click micro-animations to buttons
    const buttons = document.querySelectorAll('.btn-primary, .btn-outline, .btn-action');
    buttons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            let x = e.clientX - e.target.offsetLeft;
            let y = e.clientY - e.target.offsetTop;
            
            let ripples = document.createElement('span');
            ripples.style.left = x + 'px';
            ripples.style.top = y + 'px';
            ripples.classList.add('ripple-effect');
            
            // Add a style tag for the ripple if it doesn't exist
            if (!document.getElementById('ripple-style')) {
                const style = document.createElement('style');
                style.id = 'ripple-style';
                style.innerHTML = `
                    .ripple-effect {
                        position: absolute;
                        background: rgba(255, 255, 255, 0.25);
                        transform: translate(-50%, -50%);
                        pointer-events: none;
                        border-radius: 50%;
                        animation: ripple 0.6s linear;
                    }
                    @keyframes ripple {
                        0% { width: 0px; height: 0px; opacity: 0.5; }
                        100% { width: 500px; height: 500px; opacity: 0; }
                    }
                `;
                document.head.appendChild(style);
            }
            
            this.appendChild(ripples);
            setTimeout(() => { ripples.remove(); }, 600);
        });
    });
});
