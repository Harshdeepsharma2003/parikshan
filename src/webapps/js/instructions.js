let cameraGranted = false;
let micGranted = false;

async function requestPermissions() {
    try {
        // Request camera and microphone access
        const stream = await navigator.mediaDevices.getUserMedia({
            video: true,
            audio: true
        });

        // Update camera status
        document.getElementById('cameraStatus').textContent = 'Granted';
        document.getElementById('cameraStatus').className = 'permission-status status-granted';
        cameraGranted = true;

        // Update microphone status
        document.getElementById('micStatus').textContent = 'Granted';
        document.getElementById('micStatus').className = 'permission-status status-granted';
        micGranted = true;


        checkPermissionsAndEnableButton();


        stream.getTracks().forEach(track => track.stop());

        alert('Permissions granted successfully! You can now start the test.');

    } catch (error) {
        console.error('Permission denied:', error);


        document.getElementById('cameraStatus').textContent = 'Denied';
        document.getElementById('cameraStatus').className = 'permission-status status-denied';
        document.getElementById('micStatus').textContent = 'Denied';
        document.getElementById('micStatus').className = 'permission-status status-denied';

        alert('Camera and microphone permissions are required to take this test. Please grant permissions and try again.');
    }
}

function checkPermissionsAndEnableButton() {
    const startBtn = document.getElementById('startTestBtn');
    if (cameraGranted && micGranted) {
        startBtn.disabled = false;
        startBtn.style.background = 'var(--primary-color)';
    }
}


function loadComponent(url, where){
    fetch(url).then(r=>r.text()).then(html=>{document.getElementById(where).innerHTML = html})
        .catch(()=>console.warn('Failed to load', url));
}

document.getElementById('startTestBtn').addEventListener('click', function(e) {
    if (!cameraGranted || !micGranted) {
        e.preventDefault();
        alert('Please grant camera and microphone permissions before starting the test.');
        return;
    }

    if (!confirm('Are you ready to start the test? Make sure you have read all instructions and granted required permissions.\n\nRemember: The test will auto-submit when time expires!')) {
        e.preventDefault();
    }
});


window.addEventListener('load', async function() {
    try {
        const permissions = await navigator.permissions.query({name: 'camera'});
        if (permissions.state === 'granted') {
            // Try to access camera/mic to verify
            const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
            stream.getTracks().forEach(track => track.stop());

            // Update status
            document.getElementById('cameraStatus').textContent = 'Granted';
            document.getElementById('cameraStatus').className = 'permission-status status-granted';
            document.getElementById('micStatus').textContent = 'Granted';
            document.getElementById('micStatus').className = 'permission-status status-granted';

            cameraGranted = true;
            micGranted = true;
            checkPermissionsAndEnableButton();
        }
    } catch (error) {
        // Permissions not granted or not available
        console.log('Permissions not yet granted');
    }
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

