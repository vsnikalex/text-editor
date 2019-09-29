var PreviewClient = null;
connectPreview();

function connectPreview() {
    var socket = new SockJS('/text-editor-websocket');
    PreviewClient = Stomp.over(socket);
    PreviewClient.connect({}, function () {
        PreviewClient.subscribe('/topic/files', function (text) {
            prev(text.body);
        });
    });
}

function prev(text) {
    var p = document.getElementById("preview");
    p.value = text;
}

var fnames = document.querySelectorAll('.fname');
fnames.forEach(function(fn){
    var prev = document.getElementById("preview");

    fn.addEventListener('mouseenter', function() {
        var filepath = document.getElementById("dirpath").innerHTML + "\\" + fn.innerHTML;

        PreviewClient.send("/app/text", {}, filepath);

        prev.style.display = 'inline';
    });

    fn.addEventListener('mouseleave', function() {
        prev.style.display = 'none';
    });
});