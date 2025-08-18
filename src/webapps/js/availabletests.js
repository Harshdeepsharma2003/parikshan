
function startTest(form) {
    document.getElementById('loadingOverlay').style.display = 'grid';
    return true;      // allow form submit
}

function loadComponent(url, where){
    fetch(url).then(r=>r.text()).then(html=>{document.getElementById(where).innerHTML = html})
        .catch(()=>console.warn('Failed to load', url));
}
loadComponent('html/headerhomestudent.html','header-container');
loadComponent('html/navbarhomestudent.html','navbar-container');
loadComponent('html/footerstudenthome.html','footer-container');

setTimeout(()=>{const e=document.getElementById('errorContainer'); if(e){e.style.opacity='0'; setTimeout(()=>e.remove(),500)}},10000);
setTimeout(()=>{const s=document.getElementById('successMessage'); if(s){s.style.opacity='0'; setTimeout(()=>s.remove(),500)}},5000);
