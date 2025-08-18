<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Tests · Parikshan</title>

    <!-- Basic stylesheet -->
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/studenthome.css">
    <link rel="stylesheet" href="css/managetest.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

</head>
<body>

<!-- Header / Navbar placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

<main class="dashboard">
    <h1 class="page-title">Manage Tests</h1>

    <section class="cards">
        <!-- Edit Test Card -->
        <a href="#" onclick="return false;" class="card edit-card">
            <i class="fas fa-edit card-icon"></i>
            <h2>Edit Test</h2>
            <p>Modify existing test details and questions</p>

            <form action="ManageTest" method="post" class="card-form" onclick="event.stopPropagation();">
                <div class="form-group">
                    <label for="editTestid">Test ID</label>
                    <input type="text" id="editTestid" name="editTestid"
                           placeholder="Enter test ID" required>
                </div>

                <div class="form-group">
                    <label for="noOfQuestions">Number of Questions</label>
                    <input type="number" id="noOfQuestions" name="noOfQuestions"
                           placeholder="Enter number of questions" min="1" max="200" required>
                </div>

                <button type="submit" class="btn-primary">
                    <i class="fas fa-search"></i>
                    Verify & Edit
                </button>
            </form>
        </a>

        <!-- Delete Test Card -->
        <a href="#" onclick="return false;" class="card delete-card">
            <i class="fas fa-trash card-icon"></i>
            <h2>Delete Test</h2>
            <p>Permanently remove a test and all data</p>

            <div class="warning">
                <i class="fas fa-exclamation-triangle"></i>
                <span><strong>Warning:</strong> This action cannot be undone!</span>
            </div>

            <form action="DeleteTest" method="post" onsubmit="return confirmDelete()" class="card-form" onclick="event.stopPropagation();">
                <div class="form-group">
                    <label for="deleteTestid">Test ID</label>
                    <input type="text" id="deleteTestid" name="deleteTestid"
                           placeholder="Enter test ID to delete" required>
                </div>

                <button type="submit" class="btn-primary btn-delete">
                    <i class="fas fa-trash"></i>
                    Verify & Delete
                </button>
            </form>
        </a>
    </section>
</main>

<!-- Footer placeholder -->
<div id="footer-container"></div>

<script>
    function confirmDelete() {
        const testId = document.getElementById('deleteTestid').value;
        return confirm('Are you sure you want to delete test "' + testId + '"? This action cannot be undone.');
    }
</script>
<script src="js/managetest.js"></script>

</body>
</html>