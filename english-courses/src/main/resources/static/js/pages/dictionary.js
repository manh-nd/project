$(document).ready(function () {

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

    all.addEventListener('click', function (event) {
        searchDropdownButton.textContent = this.textContent;
        searchDropdownButton.dataset.id = 0;
        searchAll();
    });

    sentence.addEventListener('click', function (event) {
        searchDropdownButton.textContent = this.textContent;
        searchSentence();
    });

    vocabulary.addEventListener('click', function (event) {
        searchDropdownButton.textContent = this.textContent;
        searchVocabulary();
    });

    phrase.addEventListener('click', function (event) {
        searchDropdownButton.textContent = this.textContent;
        searchPhrase();
    });

    searchInput.addEventListener('keyup', function (event) {

    });

    function searchAll() {
        var value = searchInput.value;
        searchDictionary(allUrl, value, function (data) {
            console.log(data);
        }, function () {

        });
    }

    function searchSentence() {
        var value = searchInput.value;
        searchDictionary(sentenceUrl, value, function (data) {
            renderResult(data, value);
        }, function () {

        });

        function renderResult(page, value) {
            var content = page.content;
            removeAllChildren(searchResult);
            if (content && content.length) {
                for (var i = 0; i < content.length; i++) {
                    var row = document.createElement('div');
                    row.classList.add('mb-2');
                    var sentenceText = document.createElement('div');
                    var sentenceMeaning = document.createElement('div');

                    sentenceText.classList.add('text-success');
                    var text = content[i].text.trim();
                    var meaning = content[i].meaning.trim();

                    var words = value.split(' ');
                    for (var j = 0; j < words.length; j++) {
                        if (text.includes(words[j])) {
                            text = text.replace(words[j], '<strong>' + words[j] + '</strong>');
                        }
                        if (meaning.includes(words[j])) {
                            meaning = meaning.replace(words[j], '<strong>' + words[j] + '</strong>');
                        }
                    }
                    sentenceText.innerHTML = text;
                    sentenceMeaning.innerHTML = meaning;

                    row.appendChild(sentenceText);
                    row.appendChild(sentenceMeaning);
                    searchResult.appendChild(row);
                }
            } else {

            }
        }
    }

    function searchVocabulary() {
        var value = searchInput.value;
        searchDictionary(vocabularyUrl, value, function (data) {
            console.log(data);
        }, function () {

        });
    }

    function searchPhrase() {
        var value = searchInput.value;
        searchDictionary(phraseUrl, value, function (data) {
            console.log(data);
        }, function () {

        });
    }


    function searchDictionary(url, value, onSuccess, onError) {
        if (value && value.trim()) {
            $.ajax({
                url: url,
                method: 'GET',
                data: {
                    search: value
                },
                success: onSuccess,
                error: onError
            })
        }
    }


});