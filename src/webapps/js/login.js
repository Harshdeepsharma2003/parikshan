
// Load external header
fetch('html/loginheader.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('header-container').innerHTML = data;
});

// Load external navbar
fetch('html/loginnavbar.html')
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
