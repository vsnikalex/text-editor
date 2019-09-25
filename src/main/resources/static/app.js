var stompClient = null;
connect();

function connect() {
    var socket = new SockJS('/text-editor-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/files/' + hash.toString(), function (doc) {
            showText(JSON.parse(doc.body).text);
        });
    });
}

function sendText() {
    stompClient.send("/app/text/" + hash.toString(), {},
                                                    JSON.stringify({
                                                        'text': $("#editor").val(),
                                                        'file' : document.getElementById('filepath').innerHTML
                                                    }));
}

function showText(text) {
    document.getElementById("editor").value = text;
}

$(function () {
    document.getElementById("editor").oninput = function() { sendText(); };
});
