$(document).ready(function () {
    var suggestionHandler;
    var alertContainer = document.getElementById('alert-container');
    var vocabularyLesson = document.getElementById('vocabularyLesson');
    var vocabularyInput = document.getElementById('vocabularyInput');
    var sentenceInput = document.getElementById('sentenceInput');
    var addButton = document.getElementById('add-button');
    var deleteButton = document.getElementById('delete-button');
    var vocabularyDetailItemsContainer = document.getElementById('vocabularyDetailItemsContainer');

    var isSentenceChosen = false;
    var isVocabularyChosen = false;

    init();

    deleteButton.addEventListener('click', function () {
        deleteVocabularyLessonDetail(vocabularyDetailItemsContainer, this.dataset.id);
    });

    addButton.addEventListener('click', function () {
        saveVocabularyLessonDetail();
    });

    document.addEventListener('click', function () {
        clearItems(vocabularyInput);
        clearItems(sentenceInput);
    });

    function init() {
        var vocabularyLessonId = vocabularyLesson.dataset.id;
        $.ajax({
            url: '/api/v1/vocabulary-lessons/' + vocabularyLessonId,
            method: 'GET',
            dataType: 'json',
            success: function (vocabularyLesson) {
                renderVocabulariesLessonDetails(vocabularyDetailItemsContainer, vocabularyLesson);
            },
            error: function () {
                vocabularyDetailItemsContainer.parentElement.style.display = 'none';
            }
        });
        handleVocabularyInputCompletion();
        handleSentenceInputCompletion();
    }

    function handleVocabularyInputCompletion() {
        vocabularyInput.addEventListener('input', function () {
            var input = this;
            var value = this.value;
            delete input.dataset.id;
            isVocabularyChosen = false;
            clearItems(input);
            clearTimeout(suggestionHandler);
            suggestionHandler = setTimeout(search, 300, value, '/api/v1/vocabularies', function (data) {
                var content = data.content;
                if (content && content.length) {
                    var items = document.createElement('div');
                    items.classList.add('container-fluid', 'autocomplete-items');
                    for (var i = 0; i < content.length; i++) {
                        var item = document.createElement('div');
                        var leftContainer = document.createElement('div');
                        var rightContainer = document.createElement('div');
                        var image = document.createElement('img');
                        var text = document.createElement('div');
                        var meaning = document.createElement('div');

                        item.classList.add('row', 'px-2', 'py-1');
                        leftContainer.classList.add('col-1');
                        rightContainer.classList.add('col-11');
                        image.classList.add('img-fluid');

                        image.src = '/api/v1/vocabularies/' + content[i].id + '/image';
                        image.alt = 'content[i].word.text';
                        text.textContent = content[i].word.text;
                        meaning.textContent = content[i].meaning;

                        item.dataset.id = content[i].id;
                        item.dataset.text = content[i].word.text;

                        item.addEventListener('click', function (event) {
                            isVocabularyChosen = true;
                            input.dataset.id = this.dataset.id;
                            input.value = this.dataset.text;
                        });

                        leftContainer.appendChild(image);
                        rightContainer.appendChild(text);
                        rightContainer.appendChild(meaning);
                        item.appendChild(leftContainer);
                        item.appendChild(rightContainer);
                        items.appendChild(item);
                    }
                    input.parentNode.appendChild(items);
                }
            }, function (error) {
                console.log(error);
            });
        });
    }

    function handleSentenceInputCompletion(){
        sentenceInput.addEventListener('input', function () {
            var input = this;
            var value = this.value;
            delete input.dataset.id;
            isSentenceChosen = false;
            console.log('Input: ' + isSentenceChosen);
            clearItems(input);
            clearTimeout(suggestionHandler);
            suggestionHandler = setTimeout(search, 300, value, '/api/v1/sentences', function (data) {
                var content = data.content;
                if (content && content.length) {
                    var items = document.createElement('div');
                    items.classList.add('autocomplete-items');
                    for (var i = 0; i < content.length; i++) {
                        var item = document.createElement('div');
                        item.dataset.id = content[i].id;
                        item.classList.add('px-2', 'py-1');
                        item.dataset.text = content[i].text;
                        item.addEventListener('click', function (event) {
                            isSentenceChosen = true;
                            console.log('click: ' + isSentenceChosen);
                            input.dataset.id = this.dataset.id;
                            input.value = this.dataset.text;
                        });

                        var text = document.createElement('div');
                        text.textContent = content[i].text;
                        item.appendChild(text);

                        var meaning = document.createElement('div');
                        meaning.textContent = content[i].meaning;
                        item.appendChild(meaning);

                        items.appendChild(item);
                    }
                    input.parentNode.appendChild(items);
                }
            }, function (error) {
                console.log(error);
            });
        });

        sentenceInput.addEventListener('blur', function (event) {
            console.log('Sentence chosen: ' + isSentenceChosen);
        });

        sentenceInput.addEventListener('keydown', function (event) {

        });
    }

    function saveVocabularyLessonDetail() {
        var vocabularyLessonId = vocabularyLesson.dataset.id;
        var vocabularyId = vocabularyInput.dataset.id;
        var sentenceId = sentenceInput.dataset.id;
        if (!vocabularyId) {
            showAlert(alertContainer, 'danger', 'Vui lòng chọn từ vựng.');
            vocabularyInput.focus();
            return;
        }
        if (!sentenceId) {
            showAlert(alertContainer, 'danger', 'Vui lòng chọn câu.');
            sentenceInput.focus();
            return;
        }
        $.ajax({
            url: '/api/v1/vocabulary-lessons/' + vocabularyLessonId,
            method: 'PUT',
            data: JSON.stringify({
                vocabularyId: vocabularyId,
                sentenceId: sentenceId
            }),
            contentType: 'application/json',
            dataType: 'json',
            success: function (vocabularyLesson) {
                renderVocabulariesLessonDetails(vocabularyDetailItemsContainer, vocabularyLesson);
                showAlert(alertContainer, 'success', 'Thêm mới nội dung thành công.');
                clearForm();
            },
            error: function (res) {
                console.log(res);
                showAlert(alertContainer, 'danger', 'Lỗi hệ thống.');
            },
            beforeSend: function () {
                $(".preloader-backdrop").show();
            },
            complete: function () {
                $(".preloader-backdrop").fadeOut(200);
            }
        });
    }

    function search(value, url, onSuccess, onError) {
        value = value.trim();
        if (value) {
            $.ajax({
                url: url,
                method: 'GET',
                data: {
                    page: 0,
                    size: 10,
                    search: value
                },
                success: onSuccess,
                error: onError
            });
        }
    }

    function deleteVocabularyLessonDetail(container, vocabularyLessonDetailId) {
        var vocabularyLessonId = vocabularyLesson.dataset.id;
        $.ajax({
            url: '/api/v1/vocabulary-lessons/' + vocabularyLessonId + '/' + vocabularyLessonDetailId,
            method: 'DELETE',
            success: function (vocabularyLesson) {
                $('#delete-modal').modal('hide');
                showAlert(alertContainer, 'success', 'Xoá thành công.');
                renderVocabulariesLessonDetails(container, vocabularyLesson);
            },
            error: function () {
                $('#delete-modal').modal('hide');
                showAlert(alertContainer, 'danger', 'Lỗi hệ thống.');
            }
        });
    }

    function renderVocabulariesLessonDetails(container, vocabularyLesson) {
        removeAllChildren(container);
        var vocabularyLessonDetails = vocabularyLesson.vocabularyLessonDetails;
        if (vocabularyLessonDetails && vocabularyLessonDetails.length) {
            container.parentElement.style.display = 'block';
            for (var i = 0; i < vocabularyLessonDetails.length; i++) {
                var sentence = vocabularyLessonDetails[i].sentence;
                var vocabulary = vocabularyLessonDetails[i].vocabulary;

                var row = document.createElement('div');
                row.classList.add('row', 'm-2', 'p-2', 'border');
                row.style.position = 'relative';
                row.style.boxShadow = '1px 1px 3px #333333';

                // Column
                var imageColumn = document.createElement('div');
                imageColumn.classList.add('col-md-3');
                var imageElement = document.createElement('img');
                imageElement.classList.add('img-fluid');
                imageElement.src = '/api/v1/vocabularies/' + vocabulary.id + '/image';
                imageElement.alt = 'Image not available';
                imageColumn.appendChild(imageElement);
                row.appendChild(imageColumn);

                var textColumn = document.createElement('div');
                textColumn.classList.add('col-md-9');
                var wordContainer = document.createElement('div');
                var wordAudio = document.createElement('a');
                wordAudio.classList.add('mr-1');
                wordAudio.href = '/api/v1/vocabularies/' + vocabulary.id + '/audio';
                wordAudio.addEventListener('click', function (event) {
                    event.preventDefault();
                    playAudio(this);
                });
                wordAudio.appendChild(createIcon(['fa', 'fa-volume-up']));
                var word = document.createElement('strong');
                word.style.fontSize = '1.8rem';
                word.classList.add('text-warning');
                word.textContent = vocabulary.word.text;
                var ipa = document.createElement('span');
                ipa.textContent = vocabulary.word.ipa;
                wordContainer.appendChild(wordAudio);
                wordContainer.appendChild(word);
                wordContainer.appendChild(ipa);
                textColumn.appendChild(wordContainer);

                var meaning = document.createElement('div');
                meaning.textContent = vocabulary.meaning;
                textColumn.appendChild(meaning);

                var descriptionContainer = document.createElement('div');
                var wordClass = document.createElement('strong');
                wordClass.classList.add('text-warning');
                wordClass.textContent = '(' + vocabulary.wordClass.name + ')';
                descriptionContainer.appendChild(wordClass);
                var description = document.createElement('span');
                description.textContent = vocabulary.description;
                descriptionContainer.appendChild(description);
                textColumn.appendChild(descriptionContainer);

                var sentenceContainer = document.createElement('div');
                var sentenceText = document.createElement('div');

                if (sentence.audioPath) {
                    var sentenceAudio = document.createElement('a');
                    sentenceAudio.classList.add('mr-1');
                    sentenceAudio.href = '/api/v1/sentences/' + sentence.id + '/audio';
                    sentenceAudio.addEventListener('click', function (event) {
                        event.preventDefault();
                        playAudio(this);
                    });
                    sentenceAudio.appendChild(createIcon(['fa', 'fa-volume-up']));
                    sentenceText.appendChild(sentenceAudio);
                }

                sentenceText.appendChild(createSpan(function (span) {
                    span.textContent = sentence.text;
                }));
                sentenceText.classList.add('text-warning');
                sentenceText.style.fontSize = '1.2rem';
                sentenceContainer.appendChild(sentenceText);
                textColumn.appendChild(document.createElement('hr'));

                var sentenceMeaning = document.createElement('div');
                sentenceMeaning.appendChild(createSpan(function (span) {
                    span.textContent = sentence.meaning;
                }));
                sentenceContainer.appendChild(sentenceMeaning);
                textColumn.appendChild(sentenceContainer);
                row.appendChild(textColumn);

                var deleteButton = createButton(function (button) {
                    button.appendChild(createIcon(['fa', 'fa-remove']));
                    button.style.position = 'absolute';
                    button.style.top = '5px';
                    button.style.right = '5px';
                    button.classList.add('btn', 'btn-sm', 'btn-danger');
                    button.dataset.id = vocabularyLessonDetails[i].id;
                    button.addEventListener('click', function (event) {
                        var $deleteModal = $('#delete-modal');
                        $deleteModal.find('.modal-title').text('Xác nhận hệ thống');
                        $deleteModal.find('.modal-body').text('Bạn có muốn xoá nội dung này không?');
                        document.getElementById('delete-button').dataset.id = this.dataset.id;
                        $deleteModal.modal('show');
                    });
                });

                row.appendChild(deleteButton);

                container.appendChild(row);
            }
        } else {
            container.parentElement.style.display = 'none';
        }
    }

    function clearItems(input) {
        while (input.nextSibling) {
            input.nextSibling.remove();
        }
    }

    function clearForm() {
        delete vocabularyInput.dataset.id;
        vocabularyInput.value = '';
        delete sentenceInput.dataset.id;
        sentenceInput.value = '';
    }

});