<%@ page import="Servlets.HomeServlet" %>
<!DOCTYPE html>
<html lang="en">
<script>
    <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Home</title>
                <script src="/Servlets/HomeServlet.java"></script>
</head>
<body style="background: linear-gradient(to right, #4E3147 50%, #31182B 50%);">

<div style=" margin-top: 22%; margin-left: 12%;">
    <ul>
        {% for i in <%=request.getAttribute("filelist")%>%}
            <li style="list-style-type: none; margin-bottom: 20px;  background: #C0C7DA; padding: 20px; width: 300px;">
            {{ i }}
            </li>
        {% endfor %}

    </ul>
</div>

<div style="margin-left: 65%; margin-top: -30%;">

    <h1 style="margin-bottom: 250px; color: #BAC8EB; font-size: 40px;">Greetings <%=request.getAttribute("currentUser")%>
    </h1>

    <form action="" method="post" style="display: flex; flex-direction: column;">
        <input placeholder="NAME"
               style="font-size: 20px; width: 400px; border: none; background: inherit; border-bottom: 1px solid #BAC8EB; color: white; margin-bottom: 30px;"
               type="text">
        <input placeholder="ADDED BY"
               style="font-size: 20px; width: 400px; border: none; background: inherit; border-bottom: 1px solid #BAC8EB; color: white; margin-bottom: 30px;"
               type="text">
        <input placeholder="DATE"
               style="font-size: 20px; width: 400px; border: none; background: inherit; border-bottom: 1px solid #BAC8EB; color: white; margin-bottom: 30px;"
               type="text">
        <input placeholder="LOCATION"
               style="font-size: 20px; width: 400px; border: none; background: inherit; border-bottom: 1px solid #BAC8EB; color: white; margin-bottom: 30px;"
               type="text">
        <div style="text-align: center; width: 400px;">
            <button style="background: inherit; border: 1px solid #bac; padding: 14px; color: #bac; margin-right: 10px; font-weight: 600;"
                    type="submit">Download
            </button>
            <button style="background: inherit; border: 1px solid #bac; padding: 14px; color: #bac; margin-right: 10px; font-weight: 600;"
                    type="submit">Add
            </button>
            <button style="background: inherit; border: 1px solid #bac; padding: 14px; color: #bac; margin-right: 10px; font-weight: 600;"
                    type="submit">Delete
            </button>
        </div>
    </form>
</div>

</body>
</html>