
// Load external header
fetch('html/headerhomestudent.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('header-container').innerHTML = data;
});

// Load external navbar
fetch('html/navbarhomestudent.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('navbar-container').innerHTML = data;
});

// Load external footer
fetch('html/footer.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('footer-container').innerHTML = data;
});

// Form validation
document.getElementById('verifyTestForm').onsubmit = function(e) {
    const testId = this.testid.value.trim();
    const button = this.querySelector('button[type="submit"]');

    if (!testId) {
        e.preventDefault();
        showError('Please enter the Test ID.');
        return false;
    }

    // Add loading state
    button.classList.add('loading');
    button.disabled = true;

    // Allow normal form submission to servlet
};

function showError(message) {
    // Create or update error message
    let errorDiv = document.querySelector('.form-error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'form-error-message';
        document.querySelector('.form-group').appendChild(errorDiv);
    }
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';

    // Add error class to form group
    document.querySelector('.form-group').classList.add('error');

    // Remove error after 5 seconds
    setTimeout(() => {
        errorDiv.style.display = 'none';
        document.querySelector('.form-group').classList.remove('error');
    }, 5000);
}
