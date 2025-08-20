<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parikshan · Admin Dashboard</title>

    <!-- Basic stylesheet -->
    <link rel="stylesheet" href="css/landingpage.css">
    <link rel="stylesheet" href="css/studenthome.css">
    <link rel="stylesheet" href="css/adminhome.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

</head>
<body>

<!-- Header / Navbar placeholders -->
<div id="header-container"></div>
<div id="navbar-container"></div>

<main class="dashboard">
    <h1 class="page-title">Admin Dashboard</h1>


    <section class="cards">
        <!-- Card 1 -->
        <a href="createtest.jsp" class="card">
            <i class="fas fa-plus-circle card-icon"></i>
            <h2>Create Test</h2>
            <p>Design new tests and set time limits.</p>
        </a>

        <!-- Card 2 -->
        <a href="managetest.jsp" class="card">
            <i class="fas fa-edit card-icon"></i>
            <h2>Manage Tests</h2>
            <p>Edit tests and control availability.</p>
        </a>

        <!-- Card 3 -->
        <a href="DisplayStudentResults" class="card">
            <i class="fas fa-chart-line card-icon"></i>
            <h2>Student Results</h2>
            <p>View detailed performance reports.</p>
        </a>
          <a href="AdminResultsServlet" class="btn-primary mb-32">
                <i class="fas fa-chart-bar"></i>
                Recordings Results
            </a>
    </section>
</main>

<!-- Footer placeholder -->
<div id="footer-container"></div>
<script src="js/adminhome.js"></script>

</body>
</html>
