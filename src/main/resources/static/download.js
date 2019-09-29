connectDownload();

function download(filename, txt) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(txt));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}

function connectDownload() {
    var socket = new SockJS('/text-editor-websocket');
    var client = Stomp.over(socket);
    client.connect({}, function () {
        client.subscribe('/topic/downloads', function (txt) {
            var filename = document.getElementById("curFile").innerHTML;
            download(filename, txt.body);
        });
    });
}

// Start file download.
document.getElementById("dwn-btn").addEventListener("click", function(){
    // var text = document.getElementById("editor").value;
    var filename = document.getElementById("curFile").innerHTML;
    var filepath = document.getElementById("dirpath").innerHTML + "\\" + filename;

    stompClient.send("/app/download", {}, filepath);
}, false);