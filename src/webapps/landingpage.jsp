<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Contact Manager - Professional Contact Management System</title>
  <link rel="stylesheet" href="css/landingpage.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

</head>
<body>
  <!-- Header with Professional Navbar -->
  <header class="header">
    <nav class="navbar">
      <div class="nav-container">
        <div class="nav-brand">
          <i class="fas fa-address-book"></i>
          <span>ContactManager</span>
        </div>


          <a href="login.jsp" class="nav-link active">
            <i class="fas fa-users"></i>
            Contacts
          </a>

          <a href="login.jsp" class="nav-link">
            <i class="fas fa-user"></i>
            Profile
          </a>

           <a href="login.jsp" class="action-btn info">
                                     <i class="fas fa-user"></i>
                                     Login
                                   </a>
                                   <a href="register.jsp" class="action-btn custom">
                                     <i class="fas fa-user"></i>
                                     Register
                                   </a>
                 </a>
               </div>
  </div>

        </div>

        <!-- Mobile menu toggle -->
        <div class="mobile-menu-toggle">
          <i class="fas fa-bars"></i>
        </div>
      </div>
    </nav>
  </header>
<br>
    </div>
  </main>

<!-- Continuous Image Slider -->
<section class="image-slider">
  <div class="slider-track">
    <div class="slide"><img src="images/contact1.png" ></div>
    <div class="slide"><img src="images/contact2.png" ></div>
     <!-- Repeat slides for seamless loop -->
    <div class="slide"><img src="images/contact1.png" ></div>
    <div class="slide"><img src="images/contact2.png"></div>
     </div>
</section>

<!-- Professional Footer -->
<footer class="footer">
  <div class="footer-content">
    <div class="footer-container">
      <!-- Company Section -->
      <div class="footer-section">
        <div class="footer-brand">
          <i class="fas fa-address-book"></i>
          <span>ContactManager</span>
        </div>
        <p class="footer-description">
          Your trusted contact management solution for organizing and managing all your personal and professional contacts efficiently.
        </p>
        <div class="social-links">
          <a href="#" class="social-link"><i class="fab fa-facebook-f"></i></a>
          <a href="#" class="social-link"><i class="fab fa-twitter"></i></a>
          <a href="#" class="social-link"><i class="fab fa-linkedin-in"></i></a>
          <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
        </div>
      </div>

      <!-- Quick Links Section -->
      <div class="footer-section">
        <h3 class="footer-title">Quick Links</h3>
        <ul class="footer-links">
          <li><a href="index.jsp">Home</a></li>
          <li><a href="about.jsp">About Us</a></li>
          <li><a href="features.jsp">Features</a></li>
          <li><a href="contact.jsp">Contact</a></li>
          <li><a href="help.jsp">Help Center</a></li>
        </ul>
      </div>

      <!-- Services Section -->
      <div class="footer-section">
        <h3 class="footer-title">Services</h3>
        <ul class="footer-links">
          <li><a href="login.jsp">Contact Management</a></li>
          <li><a href="login.jsp">Profile Management</a></li>
          <li><a href="login.jsp">Data Export</a></li>
          <li><a href="login.jsp">Cloud Sync</a></li>
          <li><a href="login.jsp">Security Features</a></li>
        </ul>
      </div>

      <!-- Support Section -->
      <div class="footer-section">
        <h3 class="footer-title">Support</h3>
        <ul class="footer-links">
          <li><a href="faq.jsp">FAQ</a></li>
          <li><a href="support.jsp">Customer Support</a></li>
          <li><a href="privacy.jsp">Privacy Policy</a></li>
          <li><a href="terms.jsp">Terms of Service</a></li>
          <li><a href="security.jsp">Security</a></li>
        </ul>
      </div>

      <!-- Contact Info Section -->
      <div class="footer-section">
        <h3 class="footer-title">Contact Info</h3>
        <div class="contact-info">
          <div class="contact-item">
            <i class="fas fa-envelope"></i>
            <span>support@contactmanager.com</span>
          </div>
          <div class="contact-item">
            <i class="fas fa-phone"></i>
            <span>+1 (555) 123-4567</span>
          </div>
          <div class="contact-item">
            <i class="fas fa-map-marker-alt"></i>
            <span>123 Business St, Tech City, TC 12345</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Bottom Row -->
  <div class="footer-bottom">
    <div class="footer-bottom-content">
      <div class="footer-bottom-left">
        <p>&copy; 2025 ContactManager. All rights reserved.</p>
      </div>
      <div class="footer-bottom-center">
        <p>Made with <i class="fas fa-heart"></i> for better contact management</p>
      </div>
      <div class="footer-bottom-right">
        <div class="footer-bottom-links">
          <a href="privacy.jsp">Privacy</a>
          <a href="terms.jsp">Terms</a>
          <a href="cookies.jsp">Cookies</a>
        </div>
      </div>
    </div>
  </div>
</footer>
   <script src="js/landingpage.js"></script>
</body>
</html>