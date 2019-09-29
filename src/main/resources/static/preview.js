stompClient.connect({}, function () {
    stompClient.subscribe('/topic/files', function (text) {
        prev(text.body);
    });
});

function prev(text) {
    var p = document.getElementById("preview");
    p.value = text;
}

var fnames = document.querySelectorAll('.fname');
fnames.forEach(function(fn){
    var prev = document.getElementById("preview");

    fn.addEventListener('mouseenter', function() {
        var filepath = document.getElementById("dirpath").innerHTML + "\\" + fn.innerHTML;

        stompClient.send("/app/text", {}, filepath);

        prev.style.display = 'inline';
    });

    fn.addEventListener('mouseleave', function() {
        prev.style.display = 'none';
    });
});