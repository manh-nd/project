$(document).ready(function () {
    var tbody = document.querySelector("#verb-phrases-table > tbody");
    var pagination = document.querySelector("#verb-phrases-pagination");
    var searchButton = document.querySelector("#search-button");
    var searchInput = document.querySelector("#search-input");
    var apiUrl = "/api/v1/verb-phrases";
    var path = '/admin/verb-phrases/page/';
    var page = 0;
    var isSaveHistory = true;

    init();

    window.onpopstate = function (event) {
        if (event.state) {
            saveHistory = false;
            init();
        }
    };

    searchButton.addEventListener('click', function (evt) {
        isSaveHistory = true;
        search(0, getSearchInputValue());
    });

    searchInput.addEventListener('keyup', function(event){
        if (event.keyCode === 13) {
            isSaveHistory = true;
            search(0, getSearchInputValue());
        }
    });

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
            getVerbPhrasePage(page);
        }
        get(apiUrl, {page: 0, size: 10}, onSuccess, onError);
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
                    title: 'Quản lý cụm động từ tìm kiếm',
                    url: path + (currentPage + 1) + '/?search=' + searchValue
                });
            } else {
                saveHistory({
                    data: {page: currentPage},
                    title: 'Quản lý cụm động từ',
                    url: path + (currentPage + 1) + '/'
                });
            }
        }
    }

    function onError(err) {
        console.log(err)
    }

    function setPage(page) {
        if (this.page != page) {
            this.page = page;
            isSaveHistory = true;
            var text = getSearchInputValue();
            if (text) {
                search(page, text);
            } else {
                getVerbPhrasePage(page);
            }
        }
    }

    function search(page, text) {
        if (!text)
            return;
        get(apiUrl, {search: text, page: page, size: 10}, onSuccess, onError);
    }

    function getVerbPhrasePage(page) {
        get(apiUrl, {page: page, size: 10}, onSuccess, onError);
    }

    function renderTableContent(page) {
        removeAllChildren(tbody);
        var content = page.content;
        var pageable = page.pageable;
        if (content) {
            for (var i = 0; i < content.length; i++) {
                var columns = [
                    createColumn(function (td) {
                        td.textContent = pageable.offset + 1 + i;
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].text
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].ipa
                    }),
                    createColumn(function (td) {
                        var verbPhraseDetails = content[i].verbPhraseDetails;
                        var meanings = [];
                        if (verbPhraseDetails.length) {
                            for (var j = 0; j < verbPhraseDetails.length; j++) {
                                meanings.push(verbPhraseDetails[j].meaning);
                            }
                        } else {
                            td.textContent = "Không có nghĩa";
                        }
                        td.textContent = meanings.join(', ');
                    }),
                    createColumn(function (td) {
                        var icon = createIcon(['fa', 'fa-pencil']);
                        var button = document.createElement('button');
                        button.classList.add('btn', 'btn-warning');
                        button.appendChild(icon);
                        td.appendChild(button);
                    })
                ];
                createRow(tbody, columns);
            }
        } else {
            createRow(tbody, [createColumn(function(td){
                td.setAttribute('colspan', 5);
                td.textContent = 'Không có bản ghi';
            })]);
        }
    }

    function getSearchInputValue(){
        return searchInput.value.trim();
    }

});
