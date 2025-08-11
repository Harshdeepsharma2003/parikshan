let cameraGranted = false;
let micGranted = false;

/* ---------- REQUEST CAMERA AND MICROPHONE PERMISSIONS ---------- */
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

        // Enable start button if both permissions granted
        checkPermissionsAndEnableButton();

        // Stop the stream (we'll request again during actual test)
        stream.getTracks().forEach(track => track.stop());

        alert('Permissions granted successfully! You can now start the test.');

    } catch (error) {
        console.error('Permission denied:', error);

        // Update status to denied
        document.getElementById('cameraStatus').textContent = 'Denied';
        document.getElementById('cameraStatus').className = 'permission-status status-denied';
        document.getElementById('micStatus').textContent = 'Denied';
        document.getElementById('micStatus').className = 'permission-status status-denied';

        alert('Camera and microphone permissions are required to take this test. Please grant permissions and try again.');
    }
}

/* ---------- CHECK PERMISSIONS AND ENABLE START BUTTON ---------- */
function checkPermissionsAndEnableButton() {
    const startBtn = document.getElementById('startTestBtn');
    if (cameraGranted && micGranted) {
        startBtn.disabled = false;
        startBtn.style.background = 'var(--primary-color)';
    }
}

/* ---------- LOAD SHARED COMPONENTS ---------- */
function loadComponent(url, where){
    fetch(url).then(r=>r.text()).then(html=>{document.getElementById(where).innerHTML = html})
        .catch(()=>console.warn('Failed to load', url));
}
loadComponent('html/headerhomestudent.html','header-container');
loadComponent('html/navbarhomestudent.html','navbar-container');
loadComponent('html/footerstudenthome.html','footer-container');

/* ---------- CONFIRM BEFORE STARTING TEST ---------- */
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

/* ---------- CHECK EXISTING PERMISSIONS ON PAGE LOAD ---------- */
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
