
// Load external navbar
fetch('html/footer.html')
    .then(response => response.text())
    .then(data => {
    document.getElementById('footer-container').innerHTML = data;
});
