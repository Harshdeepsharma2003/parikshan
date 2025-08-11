
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

    // Form validation - Only Test ID
    const validations = {
        testid: (value) => {
            if (!value.trim()) return { valid: false, message: 'Test ID is required' };
            if (value.length < 3) return { valid: false, message: 'Test ID must be at least 3 characters' };
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
            errorElement.style.display = 'flex';
            errorElement.querySelector('span').textContent = validation.message;
            return false;
        } else {
            field.classList.remove('form-field-error');
            errorElement.style.display = 'none';
            return true;
        }
    }

    // Form submission
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        // Validate Test ID field
        let isValid = true;
        const field = document.getElementById('testid');
        if (field && !validateField(field, 'testid')) {
            isValid = false;
        }

        if (!isValid) {
            field.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }

        // Show loading
        loadingOverlay.style.display = 'flex';
        submitBtn.disabled = true;

        // Submit with timeout
        const formData = new FormData(form);

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 30000); // 30 second timeout

        fetch(form.action, {
            method: 'POST',
            body: formData,
            signal: controller.signal
        })
            .then(response => {
            clearTimeout(timeoutId);
            if (!response.ok) {
                throw new Error('Server error: ' + response.status);
            }
            return response.text();
        })
            .then(data => {
            if (data.includes('error') || data.includes('exception')) {
                throw new Error('Server returned an error');
            }
            // Success - redirect or show success
            window.location.href = 'addQuestions.jsp?testId=' + encodeURIComponent(formData.get('testid'));
        })
            .catch(error => {
            clearTimeout(timeoutId);
            loadingOverlay.style.display = 'none';
            submitBtn.disabled = false;

            let errorMessage = 'Failed to submit form. Please try again.';
            if (error.name === 'AbortError') {
                errorMessage = 'Request timed out. Please check your connection and try again.';
            }

            showErrorMessage(errorMessage, error.message);
        });
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
    });
}

// Load components
loadComponent('html/headerhomestudent.html', 'header-container');
loadComponent('html/navbarhomestudent.html', 'navbar-container');
loadComponent('html/footerhomestudent.html', 'footer-container');
