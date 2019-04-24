$(document).ready(function () {
    var tbody = document.querySelector('#vocabulary-table > tbody')
    var pagination = document.querySelector('#pagination-navigator');
    var searchButton = document.querySelector('#search-button');
    var searchInput = document.querySelector('#search-input');
    var title = 'Quản lý từ vựng';
    var path = '/admin/management/vocabularies/page/';

    var page = 0;
    var size = 30;
    var isSaveHistory = true;

    init();

    window.onpopstate = function (event) {
        if (event.state) {
            isSaveHistory = false;
            init();
        }
    };

    searchButton.addEventListener('click', function (evt) {
        isSaveHistory = true;
        search(0, getSearchInputValue());
    });

    searchInput.addEventListener('keyup', function (event) {
        if (event.keyCode === 13) {
            isSaveHistory = true;
            search(0, getSearchInputValue());
        }
    });

    function init() {
        page = 0;
        var pathname = window.location.pathname;
        var pathElements = pathname.split('/');
        if (pathElements.length && pathElements[5]) {
            page = pathElements[5] - 1;
        }
        var searchParams = new URLSearchParams(window.location.search);
        var searchParamsValue = searchParams.get('search');
        if (searchParamsValue) {
            setTextInputValue(searchParamsValue);
            search(page, getSearchInputValue());
        } else {
            getVocabularyPage(page);
        }
    }

    function reload() {
        isSaveHistory = false;
        init();
    }

    function getVocabularyPage(page) {
        $.ajax({
            method: 'GET',
            url: '/api/v1/vocabularies',
            data: {
                page: page,
                size: size
            },
            dataType: 'json',
            success: onSuccess,
            error: onError
        });
    }

    function onSuccess(response) {
        renderTableContent(response);
        paginate(pagination, response, setPage);
        if (isSaveHistory) {
            var currentPage = response.pageable.pageNumber;
            var searchValue = getSearchInputValue();
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
        console.error("err");
    }

    function search(page, text) {
        if (!text)
            return;
        $.ajax({
            method: 'GET',
            url: '/api/v1/vocabularies',
            data: {
                search: text,
                page: page,
                size: size
            },
            dataType: 'json',
            success: onSuccess,
            error: onError
        });
    }

    function setPage(page) {
        if (this.page !== page) {
            this.page = page;
            isSaveHistory = true;
            var text = getSearchInputValue();
            if (text) {
                search(page, text);
            } else {
                getVocabularyPage(this.page);
            }
        }
    }

    function setTextInputValue(value) {
        searchInput.value = value;
    }

    function getSearchInputValue() {
        return searchInput.value;
    }

    function renderTableContent(page) {
        removeAllChildren(tbody);
        var content = page.content;
        var pageable = page.pageable;
        if (content.length) {
            for (var i = 0; i < content.length; i++) {
                var columns = [
                    createColumn(function (td) {
                        td.textContent = pageable.offset + i + 1;
                    }),
                    createColumn(function (td) {
                        var img = document.createElement('img');
                        img.src = '/api/v1/vocabularies/' + content[i].id + '/image';
                        img.alt = 'Image not available';
                        img.width = 100;
                        td.appendChild(img);
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].word.text;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].wordClass.name;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].meaning;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].description;
                    }), createColumn(function (td) {
                        var icon = createIcon(['fa', 'fa-volume-up']);
                        td.classList.add('text-center');
                        var link = document.createElement('a');
                        link.href = "/api/v1/vocabularies/" + content[i].id + "/audio";
                        link.appendChild(icon);
                        link.addEventListener('click', function (event) {
                            event.preventDefault();
                            playAudio(this);
                        });
                        td.appendChild(link);
                    }), createColumn(function (td) {
                        var icon = createIcon(['fa', 'fa-pencil']);
                        var button = document.createElement('button');
                        button.dataset.id = content[i].id;
                        button.classList.add('btn', 'btn-warning', 'btn-sm');
                        button.appendChild(icon);
                        button.addEventListener('click', function (event) {
                            console.log(event);
                        });
                        td.appendChild(button);
                    })
                ];
                createRow(tbody, columns);
            }
        } else {
            createRow(tbody, [
                createColumn(function (td) {
                    td.setAttribute('colspan', '7');
                    td.textContent = "Không có bản ghi";
                })
            ]);
        }
    }

});
