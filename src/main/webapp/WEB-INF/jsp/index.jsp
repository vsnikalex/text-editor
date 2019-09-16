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
            <img src="<c:url value="/css/img/logo.png"/>" height="120" alt="label">
        </div>
        <div class="right">
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
									<a href="/?back=1">..</a>
								</span>
                            </div>
                        </li>
                        <c:forEach  items="${dirs}" var ="dir">
                            <li class="dir">
								<span>
									<a href="/?dir_name=${dir.name}">${dir.name}</a>
								</span>
                            </li>
                        </c:forEach>
                        <c:forEach  items="${filesAndIcons}" var ="entry">
                            <li class="file">
								<span>
									<img src="<c:url value="/css/img/${entry.value}"/>"/>
									<a href="/?file_name=${entry.key}">${entry.key}</a>
								</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div>
                    <form action="/" method="GET">
                        <input name="dir_name" />
                        <p><select multiple name="dir_access" style="height:20px;width:165px;margin:3px">
                            <option selected value="read_write">read and write</option>
                            <option value="read_only">read only</option>
                        </select></p>
                        <button style="height:20px;width:165px;margin:3px" type="submit" name="action" value="new_dir">New directory</button>
                    </form>
                    <form action="/" method="GET">
                        <button style="height:20px;width:165px;margin:3px" type="submit" name="action" value="rm_dir">Delete directory</button>
                    </form>
                </div>

                <div class="note_container">
                    <c:forEach  items="${dirNotes}" var ="dir_note">
                        <div id="dir_note">
                            <h4>${dir_note.header}</h4>
                            <p>${dir_note.content}</p>
                            <p align="right">
                                <i>${dir_note.modified.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</i>
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
                                <input type="radio" name="note_type" value="text_note" checked> Text note
                            </li>
                            <li>
                                <input type="radio" name="note_type" value="dir_note"> Directory note
                            </li>
                        </ul>
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
                    <c:forEach  items="${textNotes}" var ="text_note">
                        <div id="text_note">
                            <h4>${text_note.header}</h4>
                            <p>${text_note.content}</p>
                            <p align="right">
                                <i>${text_note.modified.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</i>
                            </p>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="center">
                <div class="navigation">

                    <div id="delete">
                        <form action="/" method="GET">
                            <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="delete">Delete doc</button>
                        </form>
                    </div>

                    <form action="/" method="GET">
                        <input name="file_name" id="file_name" />
                        <select multiple name="file_access" size="1" style="width:120px">
                            <option selected value="read_write">read and write</option>
                            <option value="read_only">read only</option>
                        </select>
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="new_file">New file</button>
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="save">Save</button>
                        <textarea id="editor" name="text">${text}</textarea>
                    </form>

                    <h3>${curFile}</h3>

                </div>

            </div>

        </div>
    </div>

</div>


</body>
</html>