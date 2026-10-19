// src/main/resources/static/js/app.js
// Plain JavaScript helpers for form validation and confirmation dialogs

/**
 * Validate required fields and password length (>=6).
 * Attach to forms with `onsubmit="return validateForm(this);"`.
 */
function validateForm(form) {
    const inputs = form.querySelectorAll('[required]');
    for (let input of inputs) {
        if (!input.value.trim()) {
            alert('Please fill out the "' + input.name + '" field.');
            input.focus();
            return false;
        }
        // Additional password rule
        if (input.type === 'password' && input.value.length < 6) {
            alert('Password must be at least 6 characters long.');
            input.focus();
            return false;
        }
    }
    return true;
}

/**
 * Show a simple confirmation dialog before a destructive action.
 * Use as `onclick="return confirmAction('Disable this user?');"`.
 */
function confirmAction(message) {
    return confirm(message);
}

// Optional: highlight active menu based on current URL (already handled by Thymeleaf).
