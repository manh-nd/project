$(function () {
    var alertContainer = document.getElementById('alert-container');
    $('#grammar-content').summernote();
    $('#save-grammar-lesson-button').on('click', function (event) {
        var id = $('#grammar-lesson-id').data('id');
        var title = $('#grammar-title').val();
        var content = $('#grammar-content').val();
        $.ajax({
            url: '/api/v1/grammar-lessons/' + id,
            method: 'PUT',
            data: JSON.stringify({
                id: id,
                title: title,
                content: content
            }),
            contentType: 'application/json',
            dataType: 'json',
            success: function () {
                $("html, body").animate({ scrollTop: 0 }, "fast");
                showAlert(alertContainer, 'success', 'Cập nhật bài học ngữ pháp thành công.');
            },
            error: function () {
                $("html, body").animate({ scrollTop: 0 }, "fast");
                showAlert(alertContainer, 'danger', 'Cập nhật bài học ngữ pháp thất bại.');
            }
        })
    });
});