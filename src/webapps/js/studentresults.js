
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
