<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="file_upload"/></title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background:#CCD8E0 url(img/bg.jpg) repeat-x left bottom;">

<div class="form-container" style="
    width: 500px;
    height: 500px;
    position: absolute;
    left: 50%;
    margin-left: -250px;
    top: 50%;
    margin-top: -250px;">

    <h1><spring:message code="file_upload"/></h1>
    <form method="POST" enctype="multipart/form-data" action="/uploadForm" class="form-horizontal">
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="file"><spring:message code="upload_file"/></label>
                    <div class="col-md-7">
                        <input id = "file" type="file" name="file" class="form-control input-sm"/>
                        <input type="submit" value="Upload" />
                    </div>
            </div>
        </div>
    </form>
</div>

</body>
</html>
