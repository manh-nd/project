function paginate(pagination, page, setPage) {

    var pageable = page.pageable;
    var totalPages = page.totalPages;
    var currentPage = pageable.pageNumber;
    var isFirst = page.first;
    var isLast = page.last;

    var controls;
    var start;
    var range;
    var item;
    var endPageItem;

    return init();

    function init() {
        removeAllChildren(pagination);
        controls = document.createElement('div');
        controls.classList.add('pagination', 'justify-content-center');
        pagination.appendChild(controls);
        start = 0;
        range = 5;
        item = Math.min(totalPages + start - range, Math.max(start, currentPage - (range / 2 | 0)));
        endPageItem = item + range;
        if (item < 0)
            item = 0;
        if (endPageItem) {
            renderPagination();
        }
    }

    function renderPagination() {
        addButton(0, 'First', function (pageItem) {
            if (isFirst) {
                pageItem.classList.add('disabled');
            }
        });

        addButton(currentPage - 1, 'Previous', function (pageItem) {
            if (currentPage - 1 < 0) {
                pageItem.classList.add('disabled');
            }
        });

        while (item < endPageItem) {
            addButton(item, item + 1, function (pageItem) {
                if (item === currentPage) {
                    pageItem.classList.add('active');
                }
            });
            item++;
        }

        addButton(currentPage + 1, 'Next', function (pageItem) {
            if (currentPage + 1 > totalPages - 1) {
                pageItem.classList.add('disabled');
            }
        });

        addButton(totalPages - 1, 'Last', function (pageItem) {
            if (isLast) {
                pageItem.classList.add('disabled');
            }
        });

        /**
         * Add button to pagination
         */
        function addButton(pageIndex, pageLabel, addClassToPageItem) {
            var pageButton = document.createElement('button');
            pageButton.dataset.page = pageIndex;
            pageButton.textContent = pageLabel;
            pageButton.addEventListener('click', function (event) {
                event.preventDefault();
                setPage(+event.target.dataset.page);
            });
            pageButton.classList.add('page-link');
            var pageItem = document.createElement('div');
            pageItem.classList.add('page-item');
            addClassToPageItem(pageItem);
            pageItem.appendChild(pageButton);
            controls.appendChild(pageItem);
        }
    }

}

function removeAllChildren(parentElement) {
    while (parentElement.firstChild) {
        parentElement.removeChild(parentElement.firstChild);
    }
}

function playAudio(event) {
    var url = event.srcElement.parentElement.getAttribute('href');
    $.ajax({
        method: 'GET',
        url: url,
        xhr: function () {
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        }
    }).done(success).fail(error);

    function success(response) {
        var audio = document.querySelector("#audioPlayer");
        var URL = window.URL || window.webkitURL;
        audio.src = URL.createObjectURL(response);
        audio.play();
    }

    function error(page) {
        console.error(page);
    }
}

function createIcon(classList) {
    var icon = document.createElement('i');
    var classes = icon.classList;
    if (classes) {
        for (var i = 0; i < classList.length; i++) {
            classes.add(classList[i]);
        }
    }
    return icon;
}

function saveHistory(page) {
    window.history.pushState(
        page.data,
        page.title,
        page.url
    );
}

function get(url, data, onSuccess, onError) {
    $.ajax({
        method: 'GET',
        url: url,
        data: data,
        dataType: 'json'
    }).done(onSuccess).fail(onError);
}

function createColumn(createContent) {
    var td = document.createElement('td');
    createContent(td);
    return td;
}

function createRow(tbody, columns) {
    var row = document.createElement('tr');
    for (var i = 0; i < columns.length; i++) {
        row.appendChild(columns[i]);
    }
    tbody.appendChild(row);
}
