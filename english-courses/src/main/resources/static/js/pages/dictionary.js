$(document).ready(function () {
    var alertContainer = document.getElementById('alert-container');
    var searchInput = document.getElementById('searchInput');
    var searchDropdownButton = document.getElementById('searchDropdownButton');
    var searchResult = document.getElementById('search-result');
    var all = document.getElementById('all');
    var sentence = document.getElementById('sentence');
    var vocabulary = document.getElementById('vocabulary');
    var phrase = document.getElementById('phrase');
    var allUrl = '/api/v1/dictionary/all';
    var sentenceUrl = '/api/v1/dictionary/sentence';
    var vocabularyUrl = '/api/v1/dictionary/vocabulary';
    var phraseUrl = '/api/v1/dictionary/phrase';
    var isHaveHistory = true;

    init();

    window.onpopstate = function (event) {
        if (event.state) {
            isHaveHistory = false;
            searchInput.value = event.state.value;
            searchDropdownButton.dataset.id = event.state.id;
            init();
        }
    };

    document.body.addEventListener('scroll', function (event) {
        console.log(event);
    });

    all.addEventListener('click', function () {
        setSearchType(this);
        searchAll();
    });

    sentence.addEventListener('click', function () {
        setSearchType(this);
        searchSentence();
    });

    vocabulary.addEventListener('click', function () {
        setSearchType(this);
        searchVocabulary();
    });

    phrase.addEventListener('click', function () {
        setSearchType(this);
        searchPhrase();
    });

    searchInput.addEventListener('keyup', function (event) {
        if (event.code === 'Enter') {
            search();
        }
    });

    function init() {
        search();
    }

    function setSearchType(element) {
        searchDropdownButton.textContent = element.textContent;
        searchDropdownButton.dataset.id = element.dataset.id;
        searchInput.placeholder = element.dataset.placeholder;
    }

    function search() {
        switch (searchDropdownButton.dataset.id) {
            case all.dataset.id:
                searchAll();
                break;
            case sentence.dataset.id:
                searchSentence();
                break;
            case vocabulary.dataset.id:
                searchVocabulary();
                break;
            case phrase.dataset.id:
                searchPhrase();
                break;
            default:
                break;
        }
    }

    function searchAll() {
        var value = searchInput.value;
        searchDictionary(allUrl, value, function (data) {
            renderResult(data, value);
            if (isHaveHistory)
                window.history.pushState({id: 1, value: value},
                    'Từ điển SelfLearning - Tìm tất cả',
                    '/dictionary/all?search=' + value);
            else
                isHaveHistory = true;
        }, function () {

        });

        function renderResult(data, value) {
            removeAllChildren(searchResult);
            var vocabularyPage = data.vocabularyDtoPage;
            var phrasePage = data.phraseDtoPage;
            var sentencePage = data.sentenceDtoPage;

            var row = document.createElement('div');

            var vocabularyColumn = document.createElement('div');
            var sentenceColumn = document.createElement('div');
            var phraseColumn = document.createElement('div');

            var vocabularyTitle = document.createElement('h5');
            var sentenceTitle = document.createElement('h5');
            var phraseTitle = document.createElement('h5');

            row.classList.add('row');
            vocabularyColumn.classList.add('col-md-3');
            sentenceColumn.classList.add('col-md-5');
            phraseColumn.classList.add('col-md-4');

            vocabularyTitle.textContent = "Từ vựng";
            vocabularyColumn.appendChild(vocabularyTitle);
            vocabularyColumn.appendChild(document.createElement('hr'));
            renderVocabularyResult(vocabularyPage, value, vocabularyColumn);

            sentenceTitle.textContent = "Câu";
            sentenceColumn.appendChild(sentenceTitle);
            sentenceColumn.appendChild(document.createElement('hr'));
            renderSentenceResult(sentencePage, value, sentenceColumn);

            phraseTitle.textContent = "Cụm từ";
            phraseColumn.appendChild(phraseTitle);
            phraseColumn.appendChild(document.createElement('hr'));
            renderPhraseResult(phrasePage, value, phraseColumn);

            row.appendChild(vocabularyColumn);
            row.appendChild(phraseColumn);
            row.appendChild(sentenceColumn);
            searchResult.appendChild(row);

        }
    }

    function searchSentence() {
        var value = searchInput.value;
        searchDictionary(sentenceUrl, value, function (page) {
            renderSentenceResult(page, value, searchResult);
            if (isHaveHistory)
                window.history.pushState({id: 3, value: value},
                    'Từ điển SelfLearning - Tìm câu',
                    '/dictionary/sentence?search=' + value);
            else
                isHaveHistory = true;
        }, function () {

        });
    }

    function searchVocabulary() {
        var value = searchInput.value;
        searchDictionary(vocabularyUrl, value, function (page) {
            renderVocabularyResult(page, value, searchResult);
            if (isHaveHistory)
                window.history.pushState({id: 2, value: value},
                    'Từ điển SelfLearning - Tìm từ vựng',
                    '/dictionary/vocabulary?search=' + value);
            else
                isHaveHistory = true;
        }, function () {

        });
    }

    function searchPhrase() {
        var value = searchInput.value;
        searchDictionary(phraseUrl, value, function (page) {
            renderPhraseResult(page, value, searchResult);
            if (isHaveHistory)
                window.history.pushState({id: 4, value: value},
                    'Từ điển SelfLearning - Tìm tất cả',
                    '/dictionary/phrase?search=' + value);
            else
                isHaveHistory = true;
        }, function () {

        });


    }

    function searchDictionary(url, value, onSuccess, onError) {
        if (value && value.trim()) {
            value = value.trim();
            $.ajax({
                url: url,
                method: 'GET',
                data: {
                    search: value
                },
                success: onSuccess,
                error: onError
            })
        } else {
            showAlert(alertContainer, 'danger', 'Vui lòng ' + searchInput.placeholder);
        }
    }

    function renderVocabularyResult(page, value, container) {
        var content = page.content;
        removeAllChildren(searchResult);
        if (content && content.length) {
            for (var i = 0; i < content.length; i++) {
                var row = document.createElement('div');
                var vocabularyTextContainer = document.createElement('div');
                var vocabularyText = document.createElement('span');
                var vocabularyIpaContainer = document.createElement('div');
                var vocabularyIpa = document.createElement('small');
                var vocabularyMeaning = document.createElement('div');
                var vocabularyDescription = document.createElement('div');
                var text = content[i].word.text;
                var audio = content[i].word.specialAudioPath;
                var ipa = content[i].word.ipa;
                var wordClass = content[i].wordClass.name;
                var description = content[i].description;
                var meaning = content[i].meaning;
                var words = value.trim().split(' ');

                var textMatchesLength = 0;
                var meaningMatchesLength = 0;

                var textReplace = getReplaceTextAndMatchesLength(text, words);
                var meaningReplace = getReplaceTextAndMatchesLength(meaning, words);

                text = textReplace.replaceText;
                meaning = meaningReplace.replaceText;
                textMatchesLength = textReplace.matchesLength;
                meaningMatchesLength = meaningReplace.matchesLength;

                vocabularyText.innerHTML = text;
                vocabularyIpa.textContent = " " + ipa + " ";
                if (audio) {
                    var a = document.createElement('a');
                    a.href = '/api/v1/dictionary/vocabularies/' + content[i].id + '/audio';
                    a.classList.add('ml-1');
                    a.appendChild(createIcon(['fa', 'fa-volume-up']));
                    a.addEventListener('click', function (event) {
                        event.preventDefault();
                        playAudio(this);
                    });
                    vocabularyTextContainer.appendChild(vocabularyText);
                    vocabularyTextContainer.appendChild(a);
                } else {
                    vocabularyTextContainer.appendChild(vocabularyText);
                }
                vocabularyIpaContainer.appendChild(vocabularyIpa);
                vocabularyDescription.innerHTML = '<div class="text-secondary"><small>(' + wordClass + ') </small> <small>' + description + '</small></div>';
                vocabularyMeaning.innerHTML = meaning;
                if (textMatchesLength >= meaningMatchesLength) {
                    vocabularyTextContainer.classList.add('text-primary');
                    row.appendChild(vocabularyTextContainer);
                    row.appendChild(vocabularyIpaContainer);
                    row.appendChild(vocabularyDescription);
                    row.appendChild(vocabularyMeaning);
                } else {
                    vocabularyMeaning.classList.add('text-primary');
                    row.appendChild(vocabularyMeaning);
                    row.appendChild(vocabularyTextContainer);
                    row.appendChild(vocabularyIpaContainer);
                    row.appendChild(vocabularyDescription);
                }
                row.classList.add('mb-2', 'p-2', 'border');
                container.appendChild(row);
            }
        } else {
            var noResult = document.createElement('div');
            noResult.classList.add('text-danger');
            noResult.textContent = 'Không có kết quả';
            container.appendChild(noResult);
        }
    }

    function renderPhraseResult(page, value, container) {
        var content = page.content;
        removeAllChildren(searchResult);
        if (content && content.length) {
            for (var i = 0; i < content.length; i++) {
                var row = document.createElement('div');
                var phraseText = document.createElement('div');
                var phraseDescription = document.createElement('div');
                var phraseMeaning = document.createElement('div');
                var text = content[i].text.trim();
                var phraseDetails = content[i].phraseDetails;
                var words = value.trim().split(' ');
                var textMatchesLength = 0;
                var textReplace = getReplaceTextAndMatchesLength(text, words);

                text = textReplace.replaceText;
                textMatchesLength = textReplace.matchesLength;

                for (var k = 0; k < phraseDetails.length; k++) {
                    var descriptionItem = document.createElement('div');
                    var meaningItem = document.createElement('div');
                    var description = phraseDetails[k].description;
                    var meaning = phraseDetails[k].meaning;
                    var descriptionMatchesLength = 0;
                    var meaningMatchesLength = 0;
                    var descriptionReplace = getReplaceTextAndMatchesLength(description, words);
                    var meaningReplace = getReplaceTextAndMatchesLength(meaning, words);

                    description = descriptionReplace.replaceText;
                    meaning = meaningReplace.replaceText;

                    descriptionMatchesLength = descriptionReplace.matchesLength;
                    meaningMatchesLength = meaningReplace.matchesLength;

                    phraseText.innerHTML = text;
                    if (description) {
                        descriptionItem.innerHTML = '<small>' + description + '</small>';

                    } else {
                        descriptionItem.innerHTML = '<small>' + 'no description' + '</small>';
                    }
                    meaningItem.innerHTML = meaning;

                    phraseDescription.appendChild(descriptionItem);
                    phraseMeaning.appendChild(meaningItem);
                    row.appendChild(phraseText);
                    row.appendChild(phraseDescription);
                    row.appendChild(phraseMeaning);
                }
                phraseText.classList.add('text-danger');
                phraseDescription.classList.add('text-muted');
                row.classList.add('mb-2', 'p-2', 'border');
                container.appendChild(row);
            }
        } else {
            var noResult = document.createElement('div');
            noResult.classList.add('text-danger');
            noResult.textContent = 'Không có kết quả';
            container.appendChild(noResult);
        }
    }

    function renderSentenceResult(page, value, container) {
        var content = page.content;
        removeAllChildren(searchResult);
        if (content && content.length) {
            for (var i = 0; i < content.length; i++) {
                var row = document.createElement('div');
                row.classList.add('mb-2', 'p-2', 'border');

                var sentenceTextContainer = document.createElement('div');
                var sentenceText = document.createElement('span');
                var sentenceMeaning = document.createElement('div');

                var text = content[i].text.trim();
                var meaning = content[i].meaning.trim();

                var words = value.trim().split(' ');

                var textMatchesLength = 0;
                var meaningMatchesLength = 0;

                var textReplace = getReplaceTextAndMatchesLength(text, words);
                var meaningReplace = getReplaceTextAndMatchesLength(meaning, words);

                text = textReplace.replaceText;
                meaning = meaningReplace.replaceText;
                textMatchesLength = textReplace.matchesLength;
                meaningMatchesLength = meaningReplace.matchesLength;

                sentenceText.innerHTML = text;
                sentenceMeaning.innerHTML = meaning;

                sentenceTextContainer.appendChild(sentenceText);
                if (content[i].audioPath) {
                    var sentenceAudio = document.createElement('a');
                    sentenceAudio.href = '/api/v1/sentences/' + content[i].id + "/audio";
                    sentenceAudio.appendChild(createIcon(['fa', 'fa-volume-up', 'text-success', 'ml-1']));
                    sentenceAudio.addEventListener('click', function (event) {
                        event.preventDefault();
                        playAudio(this);
                    });
                    sentenceTextContainer.appendChild(sentenceAudio);
                }

                if (textMatchesLength >= meaningMatchesLength) {
                    sentenceTextContainer.classList.add('text-success');
                    row.appendChild(sentenceTextContainer);
                    row.appendChild(sentenceMeaning);
                } else {
                    sentenceMeaning.classList.add('text-success');
                    row.appendChild(sentenceMeaning);
                    row.appendChild(sentenceTextContainer);
                }
                container.appendChild(row);
            }
        } else {
            var noResult = document.createElement('div');
            noResult.classList.add('text-danger');
            noResult.textContent = 'Không có kết quả';
            container.appendChild(noResult);
        }
    }

    function getReplaceTextAndMatchesLength(text, words) {
        var regexp = new RegExp('(\\b)(' + words.join('|') + ')(\\b)', 'gi');
        var matchesWords = text ? text.match(regexp) : '';
        var matchesLength = matchesWords ? matchesWords.length : 0;
        var replaceText = text ? text.replace(regexp, '$1<strong>$2</strong>$3') : '';
        return {
            replaceText: replaceText,
            matchesLength: matchesLength
        }
    }

});