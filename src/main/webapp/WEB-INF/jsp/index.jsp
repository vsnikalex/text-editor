<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Welcome</title>
    <link href="<c:url value="/css/main.css" />" rel="stylesheet">
</head>
<body>
<div class="top">
	<div class="header">
		<div class="left">
            ${appName}
		</div>		
	</div>	
</div>
<div class="container">	
	<div class="navigation">
		<a href="#">OPEN FILE</a>
        <a href="#">FILEPATH</a>
	</div>
	<div class="main">
        <div id="container">
            <div id="left">
                FILE NAVIGATION
            </div>
            <div id="right">
                NODE CREATOR
            </div>
            <div id="center">
                <label>
                    <textarea></textarea>
                </label>
            </div>
        </div>
	</div>
</div>
</body>
</html>