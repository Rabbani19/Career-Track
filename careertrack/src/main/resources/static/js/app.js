/* ============================================
   CareerTrack - Modern JavaScript
   ============================================ */

// ============================================
// ============================================
// Page Loader
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    setTimeout(function() {
        const loader = document.getElementById('pageLoader');
        if (loader) {
            loader.classList.add('hidden');
        }
    }, 500);
});

// ============================================
// Counter Animation
// ============================================
function animateCounter(element, target, duration = 1500) {
    let start = 0;
    const increment = target / (duration / 16);
    const timer = setInterval(() => {
        start += increment;
        if (start >= target) {
            element.textContent = target;
            clearInterval(timer);
        } else {
            element.textContent = Math.ceil(start);
        }
    }, 16);
}

function initCounters() {
    const counters = document.querySelectorAll('.stat-number');
    counters.forEach(counter => {
        const target = parseInt(counter.textContent) || 0;
        counter.textContent = '0';
        setTimeout(() => {
            animateCounter(counter, target);
        }, 500);
    });
}

// ============================================
// Toast Notifications
// ============================================
function showToast(message, type = 'success', duration = 3000) {
    const container = document.getElementById('toastContainer')
        || createToastContainer();

    const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-times-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
    };

    const colors = {
        success: '#00d4aa',
        error: '#ff6b6b',
        warning: '#ffd93d',
        info: '#4facfe'
    };

    const toast = document.createElement('div');
    toast.className = `toast-custom toast-${type}`;
    toast.style.borderLeftColor = colors[type];
    toast.innerHTML = `
        <div class="toast-icon" style="background: ${colors[type]}22; color: ${colors[type]}">
            <i class="${icons[type]}"></i>
        </div>
        <div>
            <div class="toast-title">${type.charAt(0).toUpperCase() + type.slice(1)}</div>
            <div class="toast-message">${message}</div>
        </div>
        <button onclick="this.parentElement.remove()"
                style="margin-left:auto; background:none; border:none; color:#999; cursor:pointer;">
            <i class="fas fa-times"></i>
        </button>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideInRight 0.4s ease reverse';
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container';
    document.body.appendChild(container);
    return container;
}

// ============================================
// Scroll Animations
// ============================================
function initScrollAnimations() {
    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                    observer.unobserve(entry.target);
                }
            });
        },
        { threshold: 0.1 }
    );

    document.querySelectorAll('.animate-on-scroll')
        .forEach(el => {
            el.style.opacity = '0';
            el.style.transform = 'translateY(30px)';
            el.style.transition = 'all 0.6s ease';
            observer.observe(el);
        });
}

// ============================================
// Active Nav Link
// ============================================
function setActiveNavLink() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
            link.style.background = 'rgba(255,255,255,0.25)';
            link.style.color = 'white';
        }
    });
}

// ============================================
// Password Toggle
// ============================================
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (!input || !icon) return;

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

// ============================================
// Confirm Delete
// ============================================
function confirmDelete(message = 'Are you sure you want to delete?') {
    return confirm(message);
}

// ============================================
// Auto Hide Alerts
// ============================================
function autoHideAlerts() {
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'all 0.5s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateX(100px)';
            setTimeout(() => alert.remove(), 500);
        }, 3000);
    });
}

// ============================================
// Ripple Effect on Buttons
// ============================================
function initRippleEffect() {
    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;

            ripple.style.cssText = `
                position: absolute;
                width: ${size}px;
                height: ${size}px;
                left: ${x}px;
                top: ${y}px;
                background: rgba(255,255,255,0.4);
                border-radius: 50%;
                transform: scale(0);
                animation: ripple 0.6s linear;
                pointer-events: none;
            `;

            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);

            setTimeout(() => ripple.remove(), 600);
        });
    });
}

// Add ripple keyframe
const style = document.createElement('style');
style.textContent = `
    @keyframes ripple {
        to { transform: scale(4); opacity: 0; }
    }
`;
document.head.appendChild(style);

// ============================================
// Tooltip Init
// ============================================
function initTooltips() {
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(el => {
        new bootstrap.Tooltip(el);
    });
}

// ============================================
// Initialize All
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    initCounters();
    initScrollAnimations();
    setActiveNavLink();
    autoHideAlerts();
    initRippleEffect();
    initTooltips();

    // Show success toast if flash message exists
    const successAlert = document.querySelector('.alert-success');
    if (successAlert) {
        const message = successAlert.textContent.trim();
        showToast(message, 'success');
    }

    console.log('%c🎯 CareerTrack',
        'color: #6C63FF; font-size: 20px; font-weight: bold;');
    console.log('%cModern UI Loaded Successfully!',
        'color: #00d4aa; font-size: 14px;');
});