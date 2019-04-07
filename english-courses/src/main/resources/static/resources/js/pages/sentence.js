$(document).ready(function () {
    var tbody = document.querySelector("#sentence-table > tbody");
    var pagination = document.querySelector("#pagination-navigator");
    var searchButton = document.querySelector("#search-button");
    var searchInput = document.querySelector("#search-input");
    var sentenceSaveButton = document.querySelector("#sentence-save-button");
    var sentenceAddButton = document.querySelector("#sentence-add-button");
    var audioFileButton = document.querySelector("#audioFile");
    var apiUrl = '/api/v1/sentences';
    var size = 20;
    var page = 0;
    var isSaveHistory = true;

    init();

    window.onpopstate = function (event) {
        if (event.state) {
            isSaveHistory = false;
            init();
        }
    };

    searchButton.addEventListener('click', function () {
        isSaveHistory = true;
        search(0, getSearchInputValue());
    });

    searchInput.addEventListener('keyup', function (event) {
        if (event.keyCode === 13) {
            isSaveHistory = true;
            search(0, getSearchInputValue());
        }
    });

    audioFileButton.addEventListener('change', function (event) {
        var file = event.target.files[0];
        if (file) {
            if (file.type !== 'audio/mp3') {
                alert("File không hợp lệ. Yêu cầu định dạng mp3");
                event.target.files = undefined;
                event.target.value = '';
            }
        }
    });

    sentenceSaveButton.addEventListener('click', function (event) {
        var $modal = $('#sentence-modal');
        var id = $modal.find('#id').val();
        var text = $modal.find('#text').val();
        var ipa = $modal.find('#ipa').val();
        var meaning = $modal.find('#meaning').val();
        var audioPath = $modal.find('#audioPath').val();
        var audioFile = $modal.find('#audioFile');
        var sentence = {
            id: id,
            text: text,
            ipa: ipa,
            meaning: meaning,
            audioPath: audioPath,
            audioFile: audioFile[0].files[0]
        };
        if (sentence) {
            save(sentence);
        }
    });

    sentenceAddButton.addEventListener('click', function () {
        var $modal = $('#sentence-modal');
        $modal.find('#sentence-modal-title').text('Thêm câu');
        clearForm($modal);
        $modal.modal('show');
    });

    // methods
    function init() {
        page = 0;
        var pathname = window.location.pathname;
        var pathElements = pathname.split('/');
        if (pathElements.length && pathElements[4]) {
            page = pathElements[4] - 1;
        }
        var searchParams = new URLSearchParams(window.location.search);
        var searchParam = searchParams.get('search');
        if (searchParam) {
            setSearchInputValue(searchParam);
            search(page, getSearchInputValue());
        } else {
            getSentences(page);
        }
    }

    function reload() {
        isSaveHistory = false;
        init();
    }

    function clearForm($modal) {
        $modal.find('#id').val(null);
        $modal.find('#text').val(null);
        $modal.find('#ipa').val(null);
        $modal.find('#meaning').val(null);
        $modal.find('#audioPath').val(null);
    }

    function getSentences(page) {
        get(apiUrl, {page: page, size: size}, onSuccess, onError);
    }

    function search(page, text) {
        if (!text.length) {
            var pElement = document.createElement("p");
            setTimeout(function () {
                pElement.remove();
            }, 3000);
            pElement.style.color = "red";
            pElement.textContent = "Vui lòng nhập từ khoá tìm kiếm.";
            var parent = searchInput.parentElement;
            var grandParent = parent.parentElement;
            if (parent.nextSibling) {
                grandParent.insertBefore(pElement, parent.nextSibling);
            }
            return;
        }
        if (!page)
            page = 0;
        get(apiUrl, {search: text, page: page, size: size}, onSuccess, onError);
    }

    function save(sentence) {
        var method = 'POST';
        var url = apiUrl;
        if (sentence.id) {
            url = apiUrl + "/" + sentence.id;
            method = 'PUT';
        } else {
            sentence.id = null;
        }
        $.ajax({
            method: method,
            url: url,
            data: JSON.stringify(sentence),
            contentType: 'multipart/form-data',
            dataType: 'json'
        }).done(function (response) {
            $('#sentence-modal').modal('hide');
            if (!sentence.id) {
                setSearchInputValue(response.text);
                search(0, response.text);
            } else {
                reload();
            }
        }).fail(function (response) {
            var errors = response.responseJSON.errors;
            if (errors) {
                if (errors.meaning) {
                    var meaning = document.getElementById("meaning");
                    var parent = meaning.parentElement;
                    for (var i = 0; i < errors.meaning.length; i++) {
                        var message = errors.meaning[i].message;
                        showErrorMessage(message, parent, meaning);
                    }
                }
                if (errors.text) {
                    var text = document.getElementById("text");
                    var parent = text.parentElement;
                    for (var i = 0; i < errors.text.length; i++) {
                        var message = errors.text[i].message;
                        showErrorMessage(message, parent, text);
                    }
                }
            }
        });
    }


    function onSuccess(response) {
        setTableContent(response);
        paginate(pagination, response, setPage);
        if (isSaveHistory) {
            var currentPage = response.pageable.pageNumber;
            var searchValue = getSearchInputValue();
            if (searchValue) {
                saveHistory({
                    data: {page: currentPage},
                    title: 'Danh sách câu tìm kiếm',
                    url: '/admin/sentences/page/' + (currentPage + 1) + '/?search=' + searchValue
                });
            } else {
                saveHistory({
                    data: {page: currentPage},
                    title: 'Danh sách câu tìm kiếm',
                    url: '/admin/sentences/page/' + (currentPage + 1) + '/'
                });
            }
        }
    }

    function onError(response) {
        console.error(response);
    }

    function setTableContent(page) {
        var content = page.content;
        var pageable = page.pageable;
        removeAllChildren(tbody);
        if (content.length) {
            for (var i = 0; i < content.length; i++) {
                var columns = [
                    createColumn(function (td) {
                        td.textContent = pageable.offset + 1 + i;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].text;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].ipa;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].meaning;
                    }),
                    createColumn(function (td) {
                        if (content[i].audioPath) {
                            var link = document.createElement('a');
                            var icon = document.createElement('i');
                            icon.classList.add('fa', 'fa-volume-up');
                            link.appendChild(icon);
                            link.setAttribute('href', apiUrl + '/' + content[i].id + '/audio');
                            link.addEventListener('click', function (event) {
                                event.preventDefault();
                                playAudio(event);
                            });
                            td.appendChild(link);
                        }
                        td.classList.add('text-center');
                    }),
                    createColumn(function (td) {
                        var button = document.createElement("button");
                        var icon = document.createElement('i');
                        icon.classList.add('fa', 'fa-pencil');
                        button.appendChild(icon);
                        button.dataset.sentenceId = content[i].id;
                        button.classList.add('btn', 'btn-warning', 'btn-sm');
                        button.addEventListener('click', function (event) {
                            var id = $(this).data("sentenceId");
                            var $modal = $('#sentence-modal').modal('show');
                            $.ajax({
                                method: 'GET',
                                url: apiUrl + '/' + id,
                                dataType: 'json'
                            }).done(function (data) {
                                $modal.find('#sentence-modal-title').text('Sửa câu');
                                $modal.find('#id').val(data.id);
                                $modal.find('#text').val(data.text);
                                $modal.find('#ipa').val(data.ipa);
                                $modal.find('#meaning').val(data.meaning);
                                $modal.find('#audioPath').val(data.audioPath);
                            }).fail(function (response) {
                                alert(response);
                            });
                        });
                        td.appendChild(button);
                    })
                ];
                createRow(tbody, columns);
            }
        } else {
            var columns = [createColumn(function (td) {
                td.setAttribute('colspan', '6');
                td.textContent = 'Không có bản ghi';
            })];
            createRow(tbody, columns);
        }
    }

    function setPage(page) {
        if (this.page != page) {
            this.page = page;
            isSaveHistory = true;
            if (getSearchInputValue()) {
                search(page, getSearchInputValue());
            } else {
                getSentences(this.page);
            }
        }
    }

    function getSearchInputValue() {
        return searchInput.value.trim();
    }

    function setSearchInputValue(value) {
        searchInput.value = value;
    }

});
