$(document).ready(function () {
    var tbody = document.querySelector('#vocabulary-table > tbody')
    var pagination = document.querySelector('#pagination-navigator');
    var searchBtn = document.querySelector('#searchBtn');
    var searchInput = document.queryCommandEnabled('#searchInput');
    var page = 0;
    var size = 30;
    var sort = 'word.text,asc';
    var isSaveHistory = true;

    init();

    window.onpopstate = function (event) {
        if (event.state) {
            saveHistory = false;
            init();
        }
    };

    function init() {
        page = 0;
        var pathname = window.location.pathname;
        var pathElements = pathname.split('/');
        if (pathElements.length && pathElements[4]) {
            page = pathElements[4] - 1;
        }
        var searchParams = new URLSearchParams(window.location.search);
        var searchParamsValue = searchParams.get('search');
        if (searchParamsValue) {
            searchInput.value = searchParamsValue;
            search(page, searchInput.value);
        } else {
            getVocabularyPage(page);
        }
    }

    function getVocabularyPage(page) {
        $.ajax({
            method: 'GET',
            url: '/api/v1/vocabularies',
            data: {
                page: page,
                size: size,
                sort: sort
            },
            dataType: 'json'
        }).done(onSuccess).fail(onError);
    }

    function onSuccess(response) {
        renderTableContent(response);
        paginate(pagination, response, setPage);
        if (isSaveHistory) {
            saveHistory({
                data: {page: response.pageNumber},
                title: 'Quản lý từ vựng',
                url: '/admin/vocabularies/page/' + (response.number + 1) + '/'
            });
        }
    }


    function onError(response) {
        console.error("err");
    }

    function getSearchInputValue() {
        if (searchInput.value) {
            return searchInput.value;
        }
        return null;
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
                size: size,
                sort: sort
            },
            dataType: 'json'
        }).done(onSuccess).fail(onError);
    }

    function setPage(page) {
        if (this.page != page) {
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
                            playAudio(event);
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
                addRow(columns);
            }
        } else {
            addRow(function (td) {
                td.setAttribute('colspan', '5');
                td.textContent = 'Không có dữ liệu';
            });
        }

        function createColumn(createContent) {
            var td = document.createElement('td');
            createContent(td);
            return td;
        }

        function addRow(columns) {
            var row = document.createElement('tr');
            for (var i = 0; i < columns.length; i++) {
                row.appendChild(columns[i]);
            }
            tbody.appendChild(row);
        }
    }

});
