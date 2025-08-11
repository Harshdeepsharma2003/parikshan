// Global error handler
window.addEventListener('error', function(event) {
    console.error('JavaScript Error:', event.error);
    showGlobalError('An unexpected error occurred', event.error.message);
});

// Show global error notification
function showGlobalError(message, details) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'global-error';
    errorDiv.style.cssText = 'position: fixed; top: 20px; right: 20px; background: #fee2e2; border: 1px solid #fecaca; border-radius: 8px; padding: 16px; max-width: 400px; z-index: 10000; box-shadow: 0 10px 25px rgba(0,0,0,0.1);';
    errorDiv.innerHTML =
    '<div style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;">' +
    '<i class="fas fa-exclamation-circle" style="color: #dc2626;"></i>' +
    '<strong style="color: #dc2626;">Error</strong>' +
    '</div>' +
    '<p style="margin: 0; color: #7f1d1d; font-size: 14px;">' + message + '</p>' +
    '<button onclick="this.parentElement.remove()" style="position: absolute; top: 8px; right: 8px; background: none; border: none; font-size: 18px; color: #dc2626; cursor: pointer;">&times;</button>';

    document.body.appendChild(errorDiv);
    setTimeout(() => { if (errorDiv.parentElement) errorDiv.remove(); }, 5000);
}

document.addEventListener('DOMContentLoaded', function() {
    const editForm = document.getElementById('verifyTestForm');
    const deleteForm = document.getElementById('deleteTestForm');
    const loadingOverlay = document.getElementById('loadingOverlay');

    // Hide messages after timeout
    setTimeout(() => {
        const errorContainer = document.getElementById('errorContainer');
        if (errorContainer) {
            errorContainer.style.opacity = '0';
            setTimeout(() => errorContainer.remove(), 500);
        }
    }, 8000);

    setTimeout(() => {
        const successMessage = document.getElementById('successMessage');
        if (successMessage) {
            successMessage.style.opacity = '0';
            setTimeout(() => successMessage.remove(), 500);
        }
    }, 5000);

    // Validation functions
    const validations = {
        testId: (value) => {
            if (!value.trim()) return { valid: false, message: 'Test ID is required' };
            if (value.length < 3) return { valid: false, message: 'Test ID must be at least 3 characters' };
            return { valid: true };
        },
        questions: (value) => {
            const num = parseInt(value);
            if (!value) return { valid: false, message: 'Number of questions is required' };
            if (isNaN(num) || num < 1 || num > 200) return { valid: false, message: 'Must be between 1 and 200' };
            return { valid: true };
        }
    };

    function validateField(field, validationType, errorElementId) {
        const errorElement = document.getElementById(errorElementId);
        const validation = validations[validationType](field.value);

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

    // Add validation to edit form
    const editTestIdField = document.getElementById('editTestid');
    const noOfQuestionsField = document.getElementById('noOfQuestions');

    editTestIdField.addEventListener('blur', () => {
        validateField(editTestIdField, 'testId', 'editTestid-error');
    });

    noOfQuestionsField.addEventListener('blur', () => {
        validateField(noOfQuestionsField, 'questions', 'noOfQuestions-error');
    });

    // Add validation to delete form
    const deleteTestIdField = document.getElementById('deleteTestid');
    deleteTestIdField.addEventListener('blur', () => {
        validateField(deleteTestIdField, 'testId', 'deleteTestid-error');
    });

    // Edit form submission
    editForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const testIdValid = validateField(editTestIdField, 'testId', 'editTestid-error');
        const questionsValid = validateField(noOfQuestionsField, 'questions', 'noOfQuestions-error');

        if (!testIdValid || !questionsValid) {
            const firstError = document.querySelector('.form-field-error');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
            return;
        }

        // Show loading and submit
        loadingOverlay.style.display = 'flex';
        document.getElementById('editSubmitBtn').disabled = true;

        // Submit form
        this.submit();
    });

    // Delete form submission with confirmation
    deleteForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const testIdValid = validateField(deleteTestIdField, 'testId', 'deleteTestid-error');

        if (!testIdValid) {
            deleteTestIdField.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }

        // Show confirmation dialog
        const testId = deleteTestIdField.value.trim();
        const confirmation = confirm(
            'Are you sure you want to delete test "' + testId + '"?\n\n' +
            'This will permanently remove:\n' +
            '• All test questions\n' +
            '• Student responses and results\n' +
            '• Test configuration\n\n' +
            'This action cannot be undone!'
        );

        if (!confirmation) {
            return;
        }

        // Second confirmation for safety
        const doubleConfirm = prompt(
            'To confirm deletion, please type the test ID exactly: "' + testId + '"'
        );

        if (doubleConfirm !== testId) {
            alert('Test ID does not match. Deletion cancelled for safety.');
            return;
        }

        // Show loading and submit
        loadingOverlay.style.display = 'flex';
        document.getElementById('deleteSubmitBtn').disabled = true;

        // Submit form
        this.submit();
    });
});

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
loadComponent('html/header.html', 'header-container');
loadComponent('html/navbar.html', 'navbar-container');
loadComponent('html/footer.html', 'footer-container');
