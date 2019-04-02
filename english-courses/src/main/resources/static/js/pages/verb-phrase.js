$(document).ready(function () {
    var tbody = document.querySelector("#verb-phrases-table > tbody");
    var pagination = document.querySelector("#verb-phrases-pagination");
    var searchInput = document.querySelector("#search-input");
    var searchButton = document.querySelector("#search-button");
    var baseUrl = "/api/v1/verb-phrases";
    var page = 0;
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
            getVerbPhrasePage(page);
        }
        get(baseUrl, {page: 0, size: 10}, onSuccess, onError);
    }

    function onSuccess(response) {
        renderTableContent(response);
        paginate(pagination, response, setPage);
        if (isSaveHistory) {
            saveHistory({
                data: {page: response.pageNumber},
                title: 'Quản lý cụm động từ',
                url: '/admin/verb-phrases/page/' + (response.number + 1) + '/'
            });
        }
    }

    function onError(err) {
        console.log(err)
    }

    function setPage(page){
        getVerbPhrasePage(page);
    }

    function search(){

    }

    function getVerbPhrasePage(page){
        get(baseUrl, {page: page, size: 10}, onSuccess, onError);
    }

    function renderTableContent(page) {
        removeAllChildren(tbody);
        var content = page.content;
        var pageable = page.pageable;
        console.log(content);
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
                    if(verbPhraseDetails.length){
                        for(var j = 0; j < verbPhraseDetails.length; j++){
                            meanings.push(verbPhraseDetails[j].meaning);
                        }
                    }else{
                        td.textContent = "Không có nghĩa";
                    }
                    td.textContent = meanings.join(', ');
                }),
                createColumn(function(td){
                    var icon = createIcon(['fa', 'fa-pencil']);
                    var button = document.createElement('button');
                    button.classList.add('btn', 'btn-warning');
                    button.appendChild(icon);
                    td.appendChild(button);
                })
            ];
            console.log(columns);
            createRow(tbody, columns);
        }
    }

});
