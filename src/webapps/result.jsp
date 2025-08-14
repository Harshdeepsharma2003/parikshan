<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Analysis Result</title></head>
<body>
    <h2>Video Analysis Result</h2>
    <% if (request.getAttribute("success") != null && (Boolean)request.getAttribute("success")) { %>
        <div style="background: #d4edda; padding: 10px; border-radius: 5px;">
            <h3>Analysis Complete!</h3>
            <pre><%= request.getAttribute("analysisResult") %></pre>
        </div>
    <% } else { %>
        <div style="background: #f8d7da; padding: 10px; border-radius: 5px;">
            <h3>Error:</h3>
            <p><%= request.getAttribute("error") %></p>
        </div>
    <% } %>
    <br>
    <a href="adminhome.jsp">Upload Another Video</a>
</body>
</html>
