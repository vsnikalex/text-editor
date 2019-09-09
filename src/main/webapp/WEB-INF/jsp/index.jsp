<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<head>
    <meta charset="UTF-8" />
    <title>EPAM Text Editor</title>
    <link href="<c:url value="/css/main.css" />" rel="stylesheet">
</head>
<body>
<div class="top">
    <div class="header">
        <div class="left">
            Text Editor Home Page
        </div>
    </div>
</div>
<div class="container">

    <div class="main">
        <div id="container">
            <div id="left">
                FILE NAVIGATION
            </div>
            <div id="right">

                <div>
                    <form class="note_form" action="/" method="POST">
                        <ul>
                            <li>
                                <label for="note_name">Note name:</label>
                                <input size="18" type="text" id = "note_name" name="note_name"/>
                            </li>
                            <li>
                                <label for="note_text">Note text:</label>
                                <textarea id="note_text" name="note_text" ></textarea>
                            </li>
                            <li>
                                <button style="height:25px;width:160px" class="submit" type="submit">Save note</button>
                            </li>
                        </ul>
                    </form>
                </div>

                <div id="note_container">
                    <c:forEach  items="${notes}" var ="note">
                        <div id="note">
                            <h4>${note.header}</h4>
                            <p>${note.content}</p>
                            <p align="right">
                                <i>${note.modified.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</i>
                            </p>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="center">

                <div class="navigation">
                    <form action="/" method="GET">
                        <input name="file_name"/>
                        <button style="height:25px;width:80px;margin:8px" class="submit" type="submit">Open File</button>
                        <button style="height:25px;width:80px;margin:8px" class="submit" type="submit">Save</button>
                        <textarea id="editor">${text}</textarea>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>