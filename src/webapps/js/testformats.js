
// Load external header
fetch('html/header.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('header-container').innerHTML = data;
});

// Load external navbar
fetch('html/navbar.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('navbar-container').innerHTML = data;
});

// Load external navbar
fetch('html/footer.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('footer-container').innerHTML = data;
});

document.querySelectorAll('.format-card').forEach(card => {
    card.addEventListener('click', function (e) {
        // Allow normal link behavior for navigation
        // Remove the preventDefault and custom logic
        window.location.href = card.getAttribute('href');
    });

    card.addEventListener('keypress', function (e) {
        if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            window.location.href = card.getAttribute('href');
        }
    });
});
