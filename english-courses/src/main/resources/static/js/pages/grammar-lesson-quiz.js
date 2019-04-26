$(document).ready(function () {
    var alertContainer = document.getElementById('alert-container');
    var quizFileInput = document.getElementById('quiz-file-input');
    var uploadQuizButton = document.getElementById('upload-quiz-button');
    var grammarLesson = document.getElementById('grammar-lesson-id');

    quizFileInput.addEventListener('change', function (event) {
        if(this.files[0]){
            if(this.files[0].type !== 'application/json') {
                showAlert(alertContainer, 'danger', 'Vui lòng chọn file định dạng json');
                this.files[0] = undefined;
                this.value = '';
            }
        }
    });
    uploadQuizButton.addEventListener('click', function (event) {
        var file = quizFileInput.files[0];
        if(!file){
            showAlert(alertContainer, 'danger', 'Vui lòng chọn file upload.');
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            switch (xhr.readyState) {
                case XMLHttpRequest.OPENED:
                    break;
                case XMLHttpRequest.HEADERS_RECEIVED:
                    break;
                case XMLHttpRequest.LOADING:
                    break;
                case XMLHttpRequest.DONE:
                    if(xhr.response){
                        alert("Cập nhật thành công.");
                        window.location.reload();
                    } else {
                        alert("Cập nhật thất bại.");
                    }
                    break;
                default:
                    break;
            }
        };
        xhr.responseType = 'json';
        xhr.open('PUT', '/api/v1/grammar-lesson/' + grammarLesson.dataset.id + '/questions');
        xhr.send(file);
    });
});