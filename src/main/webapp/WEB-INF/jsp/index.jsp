<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><spring:message code="title"/></title>
    <link href="<c:url value="/css/main.css" />" rel="stylesheet">
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="<c:url value="/js/app.js" />"></script>
</head>
<body>
<div class="top">
    <div class="header">
        <div class="left">
            <img src="<c:url value="/css/img/logo.png"/>" height="120" alt="label">
        </div>
        <div class="right">
            <div class="right_l"><spring:message code="greeting"/></div>
            <div class="right_r">
                <span><spring:message code="lang.change"/></span>
                <a href="/?lang=en">EN</a> | <a href="/?lang=de">DE</a>
            </div>
        </div>
    </div>
</div>


<div class="container">

    <div class="main">
        <div id="container">

            <div id="left">
                <div id="file_navigation">
                    <h4>/${curDir.name}
                        <c:if test="${readOnly=='true'}">
                            <i>[read only]</i>
                        </c:if>
                    </h4>
                    <ul>
                        <c:if test="${isRoot=='false'}">
                            <li class="dir">
                                <div>
                                     <span>
                                        <a href="/?action=go_back">..</a>
                                    </span>
                                </div>
                            </li>
                        </c:if>
                        <c:forEach  items="${dirs}" var ="dir">
                            <li class="dir">
								<span>
									<a href="/?action=open_dir&dir_name=${dir.name}">${dir.name}</a>
								</span>
                            </li>
                        </c:forEach>
                        <c:forEach  items="${filesAndIcons}" var ="entry">
                            <li class="file">
								<span>
									<img src="<c:url value="/css/img/${entry.value}"/>"/>
									<a href="/?action=open_file&file_name=${entry.key}">${entry.key}</a>
								</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div>
                    <form action="/" method="GET">
                        <input name="dir_name" placeholder="Directory name..." />
                        <p><select multiple name="dir_access" style="height:20px;width:165px;margin:3px">
                            <option selected value="read_write"><spring:message code="full_access"/></option>
                            <option value="read_only"><spring:message code="read_only"/></option>
                        </select></p>
                        <button style="height:20px;width:165px;margin:3px" type="submit" name="action" value="new_dir">
                            <spring:message code="new_directory"/>
                        </button>
                    </form>
                    <form action="/" method="GET">
                        <button style="height:20px;width:165px;margin:3px" type="submit" name="action" value="rm_dir">
                            <spring:message code="delete_directory"/>
                        </button>
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
                            <c:if test="${curFileIsDir!='true'}">
                                <li>
                                    <input type="radio" name="note_type" value="text_note" >
                                    <spring:message code="text_note"/>
                                </li>
                            </c:if>
                            <li>
                                <input type="radio" name="note_type" value="dir_note" checked>
                                <spring:message code="dir_note"/>
                            </li>
                        </ul>
                        <ul>
                            <li>
                                <label for="note_name">
                                    <spring:message code="note_name"/>
                                </label>
                                <input size="18" type="text" id = "note_name" name="note_name"/>
                            </li>
                            <li>
                                <label for="note_text">
                                    <spring:message code="note_text"/>
                                </label>
                                <textarea id="note_text" name="note_text" ></textarea>
                            </li>
                            <li>
                                <button style="height:25px;width:160px" class="submit" type="submit">
                                    <spring:message code="save_note"/>
                                </button>
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
                            <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="rm_file">
                                <spring:message code="delete_doc"/>
                            </button>
                        </form>
                    </div>

                    <form action="/" method="GET">
                        <input name="file_name" id="file_name" placeholder="File name..."/>
                        <select multiple name="file_access" size="1" style="width:120px">
                            <option selected value="read_write"><spring:message code="full_access"/></option>
                            <option value="read_only"><spring:message code="read_only"/></option>
                        </select>
                        <button style="height:25px;width:80px;margin:8px" type="submit" name="action" value="new_file">
                            <spring:message code="new_file"/>
                        </button>
                        <textarea id="editor" name="text">${text}</textarea>
                    </form>

                    <h3>${curFile.absolutePath}
                        <c:if test="${canWrite=='false'}">
                            <i>[read only]</i>
                        </c:if>
                    </h3>

                </div>

            </div>

        </div>
    </div>

</div>


</body>
</html>