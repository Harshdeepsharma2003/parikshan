
// Load external components
fetch('html/headerhomestudent.html').then(response => response.text()).then(data => {
    document.getElementById('header-container').innerHTML = data;
}).catch(error => console.error('Error loading header:', error));

fetch('html/navbarhomestudent.html').then(response => response.text()).then(data => {
    document.getElementById('navbar-container').innerHTML = data;
}).catch(error => console.error('Error loading navbar:', error));

fetch('html/footerstudenthome.html').then(response => response.text()).then(data => {
    document.getElementById('footer-container').innerHTML = data;
}).catch(error => console.error('Error loading footer:', error));

// Confirmation dialog
function confirmDeletion() {
    try {
        var studentId = document.getElementById('studentid').value.trim();
        var password = document.getElementById('password').value;
        var confirmCheckbox = document.getElementById('confirmDelete').checked;
        var messageDiv = document.getElementById('formMessage');

        // Clear previous messages
        messageDiv.innerHTML = '';
        messageDiv.className = 'form-message';

        // Validation
        if (!studentId) {
            showMessage('Please enter your Student ID.', 'error');
            return false;
        }

        if (!password) {
            showMessage('Please enter your password to confirm.', 'error');
            return false;
        }

        if (!confirmCheckbox) {
            showMessage('You must confirm that you understand this action is permanent.', 'error');
            return false;
        }

        // Final confirmation dialog
        var confirmMessage = 'Are you absolutely sure you want to delete your profile?\n\n' +
        'Student ID: ' + studentId + '\n\n' +
        'This action CANNOT be undone and will permanently delete:\n' +
        '• All your personal information\n' +
        '• Test history and results\n' +
        '• Performance data\n' +
        '• Account preferences\n\n' +
        'Type "DELETE" to confirm:';

        var userConfirm = prompt(confirmMessage);

        if (userConfirm !== 'DELETE') {
            showMessage('Deletion cancelled. You must type "DELETE" to confirm.', 'info');
            return false;
        }

        // Add loading state
        var submitBtn = document.querySelector('#deleteProfileForm button[type="submit"]');
        submitBtn.classList.add('loading');
        submitBtn.disabled = true;

        showMessage('Processing deletion...', 'info');

        // Add a small delay to show the loading state
        setTimeout(function() {
            document.getElementById('deleteProfileForm').submit();
        }, 1000);

        return false; // Prevent immediate submission
    } catch (error) {
        console.error('Confirmation error:', error);
        showMessage('An error occurred. Please try again.', 'error');
        return false;
    }
}

function showMessage(message, type) {
    var messageDiv = document.getElementById('formMessage');
    var iconMap = {
        'success': 'check-circle',
        'error': 'exclamation-circle',
        'info': 'info-circle',
        'warning': 'exclamation-triangle'
    };

    messageDiv.innerHTML = '<i class="fas fa-' + iconMap[type] + '"></i> ' + message;
    messageDiv.className = 'form-message ' + type;
    messageDiv.style.display = 'flex';
}

function goBack() {
    if (confirm('Are you sure you want to cancel? Any entered information will be lost.')) {
        window.history.back();
    }
}

// Global error handling
window.addEventListener('error', function(e) {
    console.error('Error:', e.error);
    showMessage('Something went wrong. Please refresh the page.', 'error');
});

// Add warning on page unload
window.addEventListener('beforeunload', function(e) {
    var studentId = document.getElementById('studentid').value.trim();
    var password = document.getElementById('password').value.trim();

    if (studentId || password) {
        e.preventDefault();
        e.returnValue = 'You have unsaved changes. Are you sure you want to leave?';
        return e.returnValue;
    }
});
