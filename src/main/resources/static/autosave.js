var AutosaveClient = null;
connectAutosave();

function connectAutosave() {
    var socket = new SockJS('/text-editor-websocket');
    AutosaveClient = Stomp.over(socket);
    AutosaveClient.connect({}, function () {
        AutosaveClient.subscribe('/topic/files/' + hash.toString(), function (doc) {
            showText(JSON.parse(doc.body).text);
        });
    });
}

function sendText() {
    AutosaveClient.send("/app/text/" + hash.toString(), {},
                                                    JSON.stringify({
                                                        'text': $("#editor").val(),
                                                        'file' : document.getElementById('filepath').innerHTML
                                                    }));
}

function showText(text) {
    var editor = document.getElementById("editor");

    var position = editor.selectionStart;

    editor.value = text;

    editor.selectionStart = position;
    editor.selectionEnd = position;
}

function visibility (elements, vis) {
    elements = elements.length ? elements : [elements];
    for (var index = 0; index < elements.length; index++) {
        elements[index].style.display = vis;
    }
}

$(function () {
    document.getElementById("editor").oninput = function() { sendText(); };

    $( "#hide_story" ).click(function() { visibility(document.querySelectorAll('.target'), 'none'); });
    $( "#show_story" ).click(function() { visibility(document.querySelectorAll('.target'), 'inline'); });
});
