<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
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
                <div id="file_navigation">
                    <ul>
                        <li class="dir">
                            <div>
								<span>
									<a href="/back">..</a>
								</span>
                            </div>
                        </li>
                        <c:forEach  items="${dirs}" var ="dir">
                            <li class="dir">
                                <div>
								<span>
									<a href="/open_dir">${dir.name}</a>
								</span>
                                </div>
                            </li>
                        </c:forEach>
                        <c:forEach  items="${files}" var ="file">
                            <li class="file">
                                <div>
								<span>
									<a href="/?file_name=${file.name}">${file.name}</a>
								</span>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="note_container">
                    <c:forEach  items="${notes}" var ="note">
                        <div id="file_note">
                            <h4>${note.header}</h4>
                            <p>${note.content}</p>
                            <p align="right">
                                <i>${note.modified.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</i>
                            </p>
                        </div>
                    </c:forEach>
                </div>
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
                <div class="note_container">
                    <c:forEach  items="${notes}" var ="note">
                        <div id="text_note">
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
                        <input name="file_name" id="file_name" value="${curFile}" />
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="save">Save</button>
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="delete">Delete</button>
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="new_file">New file</button>
                        <textarea id="editor" name="text">${text}</textarea>
                    </form>
                </div>
            </div>

        </div>
    </div>

</div>



</body>
</html>