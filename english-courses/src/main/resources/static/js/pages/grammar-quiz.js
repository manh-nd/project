$(document).ready(function () {
    var quizContainer = document.getElementById('quiz-container');
    var grammarLesson = document.getElementById('grammar-lesson');
    var controls = document.getElementById('controls');
    var mark = 0;
    $.ajax({
        url: '/api/v1/grammar-lessons/a7e398ac-ce71-40cf-9474-48d418cb25d7/questions',
        method: 'GET',
        dataType: 'json',
        success: function (questions) {
            renderGrammarQuiz(questions);
        },
        error: function (res) {
            console.log(res);
        }
    });

    function renderGrammarQuiz(questions) {
        for (var i in questions) {
            var questionDivElement = document.createElement('div');
            var questionTitle = document.createElement('h6');
            var answerDivElement = document.createElement('div');
            var answers = questions[i].answers;

            for (var j in answers) {
                var radioButton = document.createElement('input');
                var radioLabel = document.createElement('label');
                var answer = answers[j].answer;

                radioButton.type = 'radio';
                radioButton.name = 'answer' + [i];
                radioLabel.appendChild(radioButton);
                radioLabel.appendChild(createSpan(function (span) {
                    span.textContent = answer;
                    span.classList.add('ml-2');
                }));

                radioLabel.dataset.rightAnswer = answers[j].rightAnswer;
                radioLabel.addEventListener('click', checkResult);
                radioLabel.style.display = 'block';
                answerDivElement.appendChild(radioLabel);
            }

            questionTitle.textContent = questions[i].question;
            questionDivElement.classList.add('mb-3');
            questionDivElement.appendChild(questionTitle);
            questionDivElement.appendChild(answerDivElement);
            quizContainer.appendChild(questionDivElement);
        }

        var submitBtn = document.createElement('button');
        var resetBtn = document.createElement('button');

        submitBtn.textContent = 'Nộp bài';
        resetBtn.textContent = 'Làm lại';

        submitBtn.addEventListener('click', submitQuiz);
        resetBtn.addEventListener('click', resetQuiz);

        submitBtn.classList.add('btn', 'btn-sm', 'btn-success');
        resetBtn.classList.add('btn', 'btn-sm', 'btn-secondary', 'ml-1');

        controls.appendChild(submitBtn);
        controls.appendChild(resetBtn);
    }

    function checkResult() {
        this.firstElementChild.setAttribute('checked', 'checked');
        if (this.dataset.rightAnswer === 'true') {
            this.appendChild(createIcon(['fa', 'fa-check', 'ml-2', 'text-success']));
            mark++;
        } else {
            this.appendChild(createIcon(['fa', 'fa-close', 'ml-2', 'text-danger']));
        }
        var parentElement = this.parentElement;
        for (var i = 0; i < parentElement.childNodes.length; i++) {
            var label = parentElement.childNodes[i];
            label.removeEventListener('click', checkResult);
            label.firstElementChild.setAttribute('disabled', 'disabled');
        }
    }

    function resetQuiz() {
        var questionDivElements = quizContainer.childNodes;
        for (var i = 0; i < questionDivElements.length; i++) {
            var questionDivElement = questionDivElements[i].lastElementChild;
            for (var j = 0; j < questionDivElement.childNodes.length; j++) {
                var label = questionDivElement.childNodes[j];
                label.addEventListener('click', checkResult);
                var radio = label.firstElementChild;
                if (radio.getAttribute('checked')) {
                    radio.removeAttribute('checked');
                }
                if (radio.getAttribute('disabled')) {
                    radio.removeAttribute('disabled');
                }
                var icon = label.lastChild;
                if (icon.nodeName === 'I') {
                    icon.remove();
                }
            }
        }
        mark = 0;
    }

    function submitQuiz() {
        var questionDivElements = quizContainer.childNodes;
        for (var i = 0; i < questionDivElements.length; i++) {
            var title = questionDivElements[i].firstElementChild;
            var questionDivElement = questionDivElements[i].lastElementChild;
            for (var j = 0; j < questionDivElement.childNodes.length; j++) {
                var label = questionDivElement.childNodes[j];
                label.addEventListener('click', checkResult);
                var radio = label.firstElementChild;
                if (radio.getAttribute('checked') || radio.getAttribute('disabled')) { // da chon cau nay
                    break;
                } else {
                    questionDivElements[i].scrollIntoView();
                    var msg = showMsgWarning(title, 'Bạn chưa chọn đáp án câu này');
                    setTimeout(function () {
                        msg.remove();
                    }, 3000);
                    return;
                }
            }
        }
        var lessonModuleId = grammarLesson.dataset.lessonModuleId;
        $.ajax({
            url: '/api/v1/lesson-modules/' + lessonModuleId + "/mark",
            method: 'POST',
            data: JSON.stringify({
                lessonModule: {id: lessonModuleId},
                mark: mark,
                createdTime: new Date()
            }),
            contentType: 'application/json',
            dataType: 'json',
            success: function (res) {
                alert(JSON.stringify(res));
            },
            error: function (res) {
                alert(JSON.stringify(res));
            }
        })
    }

    function showMsgWarning(parent, message) {
        var container = document.createElement('div');
        var triangle = document.createElement('div');
        var rectangle = document.createElement('div');

        triangle.style.width = '0';
        triangle.style.height = '0';
        triangle.style.borderTop = '5px solid transparent';
        triangle.style.borderBottom = '4px solid transparent';
        triangle.style.borderLeft = '4px solid transparent';
        triangle.style.borderRight = '4px solid red';
        triangle.style.position = 'absolute';
        triangle.style.left = '-12px';
        triangle.style.top = '50%';
        triangle.style.bottom = '50%';
        triangle.style.transform = 'translateY(-50%)';

        rectangle.style.borderRadius = '5px';
        rectangle.textContent = message;
        rectangle.style.marginLeft = '-5px';
        rectangle.style.padding = '0.2rem';
        rectangle.style.borderRadius = '3px';
        rectangle.style.backgroundColor = 'red';
        rectangle.style.fontSize = '0.5rem';
        container.style.position = 'relative';
        container.style.color = 'white';
        container.style.display = 'inline-block';
        container.style.marginLeft = '1em';

        container.appendChild(triangle);
        container.appendChild(rectangle);
        parent.appendChild(container);
        return container;
    }
});