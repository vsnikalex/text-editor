<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>${pageTitle}</title>
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
                <div>
                    <form class="node_form" action="/page.html" method="POST">
                        <ul>
                            <li>
                                <label> Node name:
                                    <input size="18" type="text" name="node_name"/>
                                </label>
                            </li>
                            <li>
                                <label for="node_text">Node text:</label>
                                <textarea id="node_text" name="node_text" ></textarea>
                            </li>
                            <li>
                                <button style="height:25px;width:160px" class="submit" type="submit">Save node</button>
                            </li>
                        </ul>
                    </form>
                </div>
                <div id="node_container">
                    <div id="node">
                        <h4>nodename</h4>
                        <p>node text describing the main problem that is necessary to solve</p>
                        <p align="right"><i>dd:MM:yyyy</i></p>
                    </div>
                    <div id="node">
                        <h4>nodename</h4>
                        <p>node text describing the main problem that is necessary to solve</p>
                        <p align="right"><i>dd:MM:yyyy</i></p>
                    </div>
                    <div id="node">
                        <h4>nodename</h4>
                        <p>node text describing the main problem that is necessary to solve</p>
                        <p align="right"><i>dd:MM:yyyy</i></p>
                    </div>
                    <div id="node">
                        <h4>nodename</h4>
                        <p>node text describing the main problem that is necessary to solve</p>
                        <p align="right"><i>dd:MM:yyyy</i></p>
                    </div>
                </div>
            </div>
            <div id="center">
                <textarea id="editor">
Lorem ipsum dolor sit amet adipiscing bibendum sem orci tempus aliquet gravida, orci amet iaculis aptent blandit quam accumsan donec in facilisis, cursus ante curabitur aliquet condimentum tincidunt facilisis non cubilia lorem et pretium aliquam phasellus ipsum metus quisque auctor tristique donec nibh, praesent congue ultricies aenean ornare ligula sagittis proin sed vestibulum purus tempus aenean neque aliquam curae vivamus purus egestas ligula tincidunt nullam.

Dolor id fringilla ut lacinia sem ut pretium ante, luctus hendrerit porttitor etiam malesuada eleifend vel suscipit fusce molestie posuere venenatis pellentesque fusce eros, etiam amet est netus nostra suspendisse condimentum, nulla felis inceptos id quam velit integer orci pretium placerat maecenas ante congue purus enim sociosqu odio erat eleifend vestibulum euismod, quam convallis posuere habitasse odio vitae quisque faucibus vulputate primis integer tellus fusce.

Suscipit conubia volutpat potenti eu nostra eleifend hac neque tellus nisl, curae nunc porta turpis aptent donec litora velit elit sagittis, dolor non dapibus luctus gravida donec ultrices leo scelerisque risus eleifend vehicula morbi orci ultrices lacinia platea consectetur, dictum curabitur habitant turpis dapibus volutpat metus mollis habitasse, eget venenatis arcu congue potenti imperdiet varius.

Placerat ultrices lacus elementum eu purus, proin ullamcorper class sagittis molestie, aliquam tempor nec maecenas varius sem neque metus nostra ut tortor et auctor augue feugiat, taciti justo sapien lobortis vivamus taciti malesuada accumsan egestas.

Leo sed dolor quam feugiat ut suscipit praesent fusce bibendum magna mattis, mi laoreet eu orci integer pretium sapien litora sit quisque velit torquent ut aenean per conubia velit, dictum gravida viverra nibh curabitur, donec platea lobortis leo tincidunt.

Interdum congue class ipsum suspendisse eu libero malesuada lobortis facilisis, leo platea tempor ad sit nisi dapibus aliquam nibh, integer pellentesque commodo tellus ipsum ut facilisis aliquam bibendum hac bibendum quis nulla sodales augue himenaeos ipsum felis donec dapibus etiam congue.

Vel eget porta auctor at curabitur taciti molestie aenean at, eget lacus facilisis quisque libero tortor ipsum mattis purus, ante sapien aliquam tristique dictumst varius nulla lorem sed diam luctus donec vitae ultrices, vel lectus elementum ut eros, inceptos eget accumsan felis himenaeos tempor torquent nostra vulputate sodales habitasse imperdiet nullam leo, tincidunt molestie condimentum donec tristique magna non donec.

Turpis enim praesent condimentum amet senectus convallis velit cras lobortis massa conubia aliquam molestie, posuere orci bibendum congue varius etiam aliquet conubia adipiscing massa donec vivamus nostra egestas mauris egestas at sagittis justo, aptent habitasse odio sodales pharetra nam, dui etiam bibendum malesuada vehicula dictum.

Sit integer adipiscing nulla etiam diam blandit placerat praesent purus quis habitasse, adipiscing scelerisque nullam scelerisque felis sem himenaeos pulvinar massa faucibus, ut dolor velit sed erat inceptos auctor ante et aptent curae arcu purus condimentum mollis praesent ipsum nibh rhoncus eros, non per lacinia rutrum sem nunc ac aptent suspendisse, dictumst lorem nullam dui habitant libero felis ut.
				</textarea>
            </div>
        </div>
    </div>
</div>
</body>
</html>