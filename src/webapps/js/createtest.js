// Global error handler
window.addEventListener('error', function(event) {
    console.error('JavaScript Error:', event.error);
    showGlobalError('An unexpected error occurred', event.error.message);
});

// Global unhandled promise rejection handler
window.addEventListener('unhandledrejection', function(event) {
    console.error('Unhandled Promise Rejection:', event.reason);
    showGlobalError('Network or processing error', event.reason);
});

// Show global error notification
function showGlobalError(message, details) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'global-error';
    errorDiv.innerHTML =
    '<div style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;">' +
    '<i class="fas fa-exclamation-circle" style="color: #dc2626;"></i>' +
    '<strong style="color: #dc2626;">Error</strong>' +
    '</div>' +
    '<p style="margin: 0; color: #7f1d1d; font-size: 14px;">' + message + '</p>' +
    '<button onclick="this.parentElement.remove()" style="position: absolute; top: 8px; right: 8px; background: none; border: none; font-size: 18px; color: #dc2626; cursor: pointer;">&times;</button>';

    document.body.appendChild(errorDiv);

    // Auto remove after 5 seconds
    setTimeout(() => {
        if (errorDiv.parentElement) {
            errorDiv.remove();
        }
    }, 5000);
}

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerTestForm');
    const submitBtn = document.getElementById('submitBtn');
    const loadingOverlay = document.getElementById('loadingOverlay');

    // Hide messages after timeout
    setTimeout(() => {
        const errorContainer = document.getElementById('errorContainer');
        if (errorContainer) {
            errorContainer.style.opacity = '0';
            setTimeout(() => errorContainer.remove(), 500);
        }
    }, 10000);

    setTimeout(() => {
        const successMessage = document.getElementById('successMessage');
        if (successMessage) {
            successMessage.style.opacity = '0';
            setTimeout(() => successMessage.remove(), 500);
        }
    }, 5000);

    // Form validation
    const validations = {
        testid: (value) => {
            if (!value.trim()) return { valid: false, message: 'Test ID is required' };
            if (value.length < 3) return { valid: false, message: 'Test ID must be at least 3 characters' };
            return { valid: true };
        },
        testTitle: (value) => {
            if (!value.trim()) return { valid: false, message: 'Test title is required' };
            if (value.length < 5) return { valid: false, message: 'Title must be at least 5 characters' };
            return { valid: true };
        },
        noofquestions: (value) => {
            const num = parseInt(value);
            if (!value) return { valid: false, message: 'Number of questions is required' };
            // Updated validation for 5 question limit
            if (isNaN(num) || num < 1 || num > 5) return { valid: false, message: 'Must be between 1 and 5' };
            return { valid: true };
        }
    };

    // Add validation listeners
    Object.keys(validations).forEach(fieldName => {
        const field = document.getElementById(fieldName);
        if (field) {
            field.addEventListener('blur', () => validateField(field, fieldName));
            field.addEventListener('input', () => {
                if (field.classList.contains('form-field-error')) {
                    validateField(field, fieldName);
                }
            });
        }
    });

    function validateField(field, fieldName) {
        const errorElement = document.getElementById(fieldName + '-error');
        const validation = validations[fieldName](field.value);

        if (!validation.valid) {
            field.classList.add('form-field-error');
            if (errorElement) {
                errorElement.style.display = 'flex';
                const spanElement = errorElement.querySelector('span');
                if (spanElement) {
                    spanElement.textContent = validation.message;
                }
            }
            return false;
        } else {
            field.classList.remove('form-field-error');
            if (errorElement) {
                errorElement.style.display = 'none';
            }
            return true;
        }
    }

    // Form submission - FIXED: Let form submit naturally instead of using fetch
    form.addEventListener('submit', function(e) {
        console.log('Form submission started');

        // Validate all fields first
        let isValid = true;
        Object.keys(validations).forEach(fieldName => {
            const field = document.getElementById(fieldName);
            if (field && !validateField(field, fieldName)) {
                isValid = false;
            }
        });

        if (!isValid) {
            e.preventDefault();
            const firstError = document.querySelector('.form-field-error');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
            return false;
        }

        // Show loading overlay
        if (loadingOverlay) {
            loadingOverlay.style.display = 'flex';
        }

        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = 'Processing... <i class="fas fa-spinner fa-spin"></i>';
        }

        console.log('Form validation passed, submitting naturally...');

        // Let the form submit naturally to the servlet
        // Don't prevent default - let the browser handle the POST request
        return true;
    });

    // Prevent multiple submissions
    let isSubmitting = false;
    form.addEventListener('submit', function(e) {
        if (isSubmitting) {
            e.preventDefault();
            return false;
        }
        isSubmitting = true;
    });
});

// Function to show error messages dynamically
function showErrorMessage(message, details) {
    const existingError = document.getElementById('errorContainer');
    if (existingError) {
        existingError.remove();
    }

    let errorHTML = '<div class="error-container" id="errorContainer">' +
    '<div class="error-header">' +
    '<i class="fas fa-exclamation-triangle error-icon"></i>' +
    '<h3 class="error-title">Something went wrong</h3>' +
    '</div>' +
    '<p class="error-message">' + message + '</p>';

    if (details) {
        errorHTML += '<details class="error-details">' +
        '<summary>Error Details</summary>' +
        '<div class="error-code">' + details + '</div>' +
        '</details>';
    }

    errorHTML += '<div class="error-actions">' +
    '<button type="button" class="btn-error" onclick="location.reload()">' +
    '<i class="fas fa-redo"></i> Try Again' +
    '</button>' +
    '<a href="landingpage.jsp" class="btn-secondary-error">' +
    '<i class="fas fa-home"></i> Back to Home' +
    '</a>' +
    '<button type="button" class="btn-secondary-error" onclick="reportError()">' +
    '<i class="fas fa-bug"></i> Report Issue' +
    '</button>' +
    '</div></div>';

    const formContainer = document.querySelector('.form-container');
    if (formContainer) {
        formContainer.insertAdjacentHTML('afterbegin', errorHTML);
    }
}

// Error reporting function
function reportError() {
    const errorDetails = document.querySelector('.error-code');
    const errorText = errorDetails ? errorDetails.textContent : 'No details available';

    if (navigator.clipboard) {
        navigator.clipboard.writeText(errorText).then(() => {
            alert('Error details copied to clipboard. Please paste this when contacting support.');
        }).catch(() => {
            alert('Please copy the error details manually and send to support.');
        });
    } else {
        alert('Please copy the error details manually and send to support.');
    }
}

// Load external components with error handling
function loadComponent(url, containerId) {
    fetch(url)
        .then(response => {
        if (!response.ok) throw new Error('Failed to load ' + url);
        return response.text();
    })
        .then(data => {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = data;
        }
    })
        .catch(error => {
        console.warn('Failed to load component:', url, error);
        // Don't show error for missing components
    });
}

// Load components - these are optional
try {
    loadComponent('html/headerhomestudent.html', 'header-container');
    loadComponent('html/navbarhomestudent.html', 'navbar-container');
    loadComponent('html/footerhomestudent.html', 'footer-container');
} catch (error) {
    console.warn('Some components could not be loaded:', error);
}