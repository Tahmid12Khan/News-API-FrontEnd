$(document).ready(function () {

    $(document).on("click", '[name=delete]', function () {
        var ok = confirm("Are you sure you want to delete it?");
        if (ok == false) return;
        var workingObject = $(this).parent();
        var newsId = workingObject.find('input').val();
        var newsTitle = workingObject.find('h2').text();
        $.ajax({
            type: "DELETE",
            url: "/delete-news/" + newsId,
            success: function () {
                workingObject.empty();
                workingObject.html("<b>" + newsTitle + " has been Deleted</b>");
            },
            error: function (e) {
                alert("ERROR: ", e);
                console.log("ERROR: ", e);
            }
        });
    });

    $(document).on("click", '[name=del]', function () {
        var ok = confirm("Are you sure you want to delete it?");
        if (ok == false) return;
        var workingObject = $("#id");
        var newsId = workingObject.text();
        var newsTitle = $("#title").text();
        $.ajax({
            type: "DELETE",
            url: "/delete-news/" + newsId,
            success: function () {
                alert(newsTitle + " has been Deleted.");
                $("<a href='/'></a>").click();
                window.location.href = window.location.host;

            },
            error: function (e) {
                alert("ERROR: ", e);
                console.log("ERROR: ", e);
            }
        });
    });
});