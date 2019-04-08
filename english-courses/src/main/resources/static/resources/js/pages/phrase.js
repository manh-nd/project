$(document).ready(function () {
    const tbody = document.querySelector("#phrases-table > tbody");
    const pagination = document.querySelector("#phrases-pagination");
    const searchButton = document.querySelector("#search-button");
    const searchInput = document.querySelector("#search-input");
    const apiUrl = "/api/v1/phrases";
    const title = 'Quản lý cụm từ';
    const path = '/admin/management/phrases/page/';
    let page = 0;
    let size = 20;
    let isSaveHistory = true;

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

    searchInput.addEventListener('keyup', function (event) {
        if (event.keyCode === 13) {
            isSaveHistory = true;
            search(0, getSearchInputValue());
        }
    });

    function init() {
        page = 0;
        let pathname = window.location.pathname;
        let pathElements = pathname.split('/');
        if (pathElements.length && pathElements[4]) {
            page = pathElements[4] - 1;
        }
        let searchParams = new URLSearchParams(window.location.search);
        let searchParamsValue = searchParams.get('search');
        if (searchParamsValue) {
            searchInput.value = searchParamsValue;
            search(page, searchInput.value);
        } else {
            getPhrasePage(page);
        }
    }

    function onSuccess(response) {
        renderTableContent(response);
        paginate(pagination, response, setPage);
        if (isSaveHistory) {
            let currentPage = response.pageable.pageNumber;
            let searchValue = getSearchInputValue();
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

    function onError(err) {
        console.log(err)
    }

    function setPage(page) {
        if (this.page != page) {
            this.page = page;
            isSaveHistory = true;
            let text = getSearchInputValue();
            if (text) {
                search(page, text);
            } else {
                getPhrasePage(page);
            }
        }
    }

    function search(page, text) {
        if (!text)
            return;
        get(apiUrl, {search: text, page: page, size: size}, onSuccess, onError);
    }

    function getPhrasePage(page) {
        get(apiUrl, {page: page, size: size}, onSuccess, onError);
    }

    function renderTableContent(page) {
        removeAllChildren(tbody);
        let content = page.content;
        let pageable = page.pageable;
        if (content) {
            for (let i = 0; i < content.length; i++) {
                let columns = [
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
                        let phraseDetails = content[i].phraseDetails;
                        let meanings = [];
                        if (phraseDetails.length) {
                            for (let j = 0; j < phraseDetails.length; j++) {
                                meanings.push(phraseDetails[j].meaning);
                            }
                        } else {
                            td.textContent = "Không có nghĩa";
                        }
                        td.textContent = meanings.join(', ');
                    }),
                    createColumn(function (td) {
                        let icon = createIcon(['fa', 'fa-pencil']);
                        let button = document.createElement('button');
                        button.classList.add('btn', 'btn-warning');
                        button.appendChild(icon);
                        td.appendChild(button);
                    })
                ];
                createRow(tbody, columns);
            }
        } else {
            createRow(tbody, [createColumn(function (td) {
                td.setAttribute('colspan', 5);
                td.textContent = 'Không có bản ghi';
            })]);
        }
    }

    function getSearchInputValue() {
        return searchInput.value.trim();
    }

});
