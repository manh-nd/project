$(document).ready(function () {
    const tbody = document.querySelector("#sentence-table > tbody");
    const pagination = document.querySelector("#pagination-navigator");
    const searchButton = document.querySelector("#search-button");
    const searchInput = document.querySelector("#search-input");
    const sentenceSaveButton = document.querySelector("#sentence-save-button");
    const sentenceAddButton = document.querySelector("#sentence-add-button");
    const audioFileButton = document.querySelector("#audioFile");
    const apiUrl = '/api/v1/sentences';

    const title = 'Quản lý câu';
    const path = '/admin/management/sentences/page/';

    let size = 20;
    let page = 0;
    let isSaveHistory = true;

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
        let file = event.target.files[0];
        if (file) {
            if (file.type !== 'audio/mp3') {
                alert("File không hợp lệ. Yêu cầu định dạng mp3");
                event.target.files = undefined;
                event.target.value = '';
            }
        }
    });

    sentenceSaveButton.addEventListener('click', function () {
        const $modal = $('#sentence-modal');
        const id = $modal.find('#id').val();
        const text = $modal.find('#text').val();
        const ipa = $modal.find('#ipa').val();
        const meaning = $modal.find('#meaning').val();
        const audioPath = $modal.find('#audioPath').val();
        const audioFile = $modal.find('#audioFile');
        const sentence = {
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
        let $modal = $('#sentence-modal');
        $modal.find('#sentence-modal-title').text('Thêm câu');
        clearForm($modal);
        $modal.modal('show');
    });

    // methods
    function init() {
        page = 0;
        const pathname = window.location.pathname;
        const pathElements = pathname.split('/');
        if (pathElements.length && pathElements[4]) {
            page = pathElements[4] - 1;
        }
        const searchParams = new URLSearchParams(window.location.search);
        const searchParam = searchParams.get('search');
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
            const pElement = document.createElement("p");
            setTimeout(function () {
                pElement.remove();
            }, 3000);
            pElement.style.color = "red";
            pElement.textContent = "Vui lòng nhập từ khoá tìm kiếm.";
            const parent = searchInput.parentElement;
            const grandParent = parent.parentElement;
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
            const errors = response.responseJSON.errors;
            if (errors) {
                if (errors.meaning) {
                    const meaning = document.getElementById("meaning");
                    const parent = meaning.parentElement;
                    for (let i = 0; i < errors.meaning.length; i++) {
                        const message = errors.meaning[i].message;
                        showErrorMessage(message, parent, meaning);
                    }
                }
                if (errors.text) {
                    const text = document.getElementById("text");
                    const parent = text.parentElement;
                    for (let i = 0; i < errors.text.length; i++) {
                        const message = errors.text[i].message;
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
            const currentPage = response.pageable.pageNumber;
            const searchValue = getSearchInputValue();
            if (searchValue) {
                saveHistory({
                    data: {page: currentPage},
                    title: title,
                    url: path + (currentPage + 1) + '/?search=' + searchValue
                });
            } else {
                saveHistory({
                    data: {page: currentPage},
                    title: title,
                    url: path + (currentPage + 1) + '/'
                });
            }
        }
    }

    function onError(response) {
        console.error(response);
    }

    function setTableContent(page) {
        let content = page.content;
        let pageable = page.pageable;
        removeAllChildren(tbody);
        if (content.length) {
            for (let i = 0; i < content.length; i++) {
                const columns = [
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
                            const link = document.createElement('a');
                            const icon = document.createElement('i');
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
                        const button = document.createElement("button");
                        const icon = document.createElement('i');
                        icon.classList.add('fa', 'fa-pencil');
                        button.appendChild(icon);
                        button.dataset.sentenceId = content[i].id;
                        button.classList.add('btn', 'btn-warning', 'btn-sm');
                        button.addEventListener('click', function () {
                            const id = $(this).data("sentenceId");
                            const $modal = $('#sentence-modal').modal('show');
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
            const columns = [createColumn(function (td) {
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
