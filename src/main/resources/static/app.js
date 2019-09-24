var stompClient = null;
connect();

function connect() {
    var socket = new SockJS('/text-editor-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/files', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function sendText() {
    stompClient.send("/app/text", {}, JSON.stringify({  'text': $("#editor").val(),
                                                               'file' : document.getElementById('filepath').innerHTML }));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $( "#editor" ).keydown(function() { sendText(); });
});
