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
                    <form class="node_form" action="/" method="POST">
                        <ul>
                            <li>
                                <label> Note name:
                                    <input size="18" type="text" name="note_name"/>
                                </label>
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

                <div id="node_container">
                    <c:forEach  items="${notes}" var ="note">
                    <div id="node">
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
                        <button style="height:25px;width:160px;margin:8px" class="submit" type="submit">Open File</button>
                    </form>
                </div>

                <textarea id="editor">${fileName}</textarea>

            </div>
        </div>
    </div>
</div>
</body>
</html>