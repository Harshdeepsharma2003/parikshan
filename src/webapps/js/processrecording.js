// Simple form validation and loading state
document.getElementById('processForm').addEventListener('submit', function(e) {
    const recordingId = document.getElementById('recordingId').value;

    // Basic validation
    if (!recordingId || recordingId <= 0) {
        alert('Please enter a valid recording ID');
        e.preventDefault();
        return;
    }

    // Show loading state
    const processBtn = document.getElementById('processBtn');
    const loadingDiv = document.getElementById('loadingDiv');

    processBtn.disabled = true;
    processBtn.textContent = 'Processing...';
    loadingDiv.style.display = 'block';
});


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
fetch('html/footerstudenthome.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('footer-container').innerHTML = data;
});