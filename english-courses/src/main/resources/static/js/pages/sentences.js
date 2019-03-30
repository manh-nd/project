$(document).ready(function () {
    var tbody = document.querySelector("#sentence-body");
    var pagination = document.querySelector("#pagination");
    var url = '/api/v1/sentences';
    var size = 20;
    var page = 0;
    var audio = document.querySelector("#audioPlayer");
    var playing = false;
    var pause = true;
    var saveHistory = true;

    audio.playing = function () {
        console.log("playing...")
        playing = true;
        pause = false;
    };

    audio.pause = function () {
        console.log("paused")
        pause = true;
        playing = false;
    };

    init();

    $("#searchBtn").on('click', function (event) {
        var text = $("#searchBox").val();
        saveHistory = true;
        search(0, text);
    });

    $("#searchBox").on('keyup', function (event) {
        var text = $(this).val();
        if (event.keyCode === 13) {
            saveHistory = true;
            search(0, text);
        }
    });

    $('#saveSentenceBtn').on('click', function (event) {
        var $modal = $('#sentenceModal');
        var id = $modal.find('#id').val();
        var text = $modal.find('#text').val();
        var ipa = $modal.find('#ipa').val();
        var meaning = $modal.find('#meaning').val();
        var audioPath = $modal.find('#audioPath').val();
        var sentence = {
            id: id,
            text: text,
            ipa: ipa,
            meaning: meaning,
            audioPath: audioPath
        };
        if (sentence) {
            save(sentence);
        }
    });

    $('#sentenceAddButton').on('click', function () {
        var $modal = $('#sentenceModal');
        $modal.find('#sentenceModalTitle').text('Thêm câu');
        clearForm($modal);
        $modal.modal('show');
    });

    function init() {
        page = 0;
        var pathname = window.location.pathname;
        var pathElements = pathname.split('/');
        if (pathElements.length && pathElements[4]) {
            page = pathElements[4] - 1;
        }
        var searchParams = new URLSearchParams(window.location.search);
        if (searchParams.get('search')) {
            $("#searchBox").val(searchParams.get('search'));
            search(page, searchParams.get('search'));
        } else {
            getSentences(page);
        }
    }

    function clearForm($modal) {
        $modal.find('#id').val(null);
        $modal.find('#text').val(null);
        $modal.find('#ipa').val(null);
        $modal.find('#meaning').val(null);
        $modal.find('#audioPath').val(null);
    }

    window.onpopstate = function (event) {
        if (event.state) {
            saveHistory = false;
            init();
        }
    };

    function getSentences(page) {
        $.ajax({
            method: 'GET',
            url: url,
            data: {
                page: page,
                size: size
            },
            dataType: 'json'
        }).done(onSuccess).fail(onError);
    }

    function getText() {
        return $("#searchBox").val().trim();
    }

    function search(page, text) {
        if (!text.length) return;
        if (!page)
            page = 0;
        $.ajax({
            method: 'GET',
            url: '/api/v1/sentences',
            data: {
                search: text,
                page: page,
                size: size
            },
            dataType: 'json'
        }).done(onSuccess).fail(onError);
    }

    function save(sentence) {
        var method = 'POST';
        var url = '/api/v1/sentences';
        if (sentence.id) {
            url = '/api/v1/sentences/' + sentence.id;
            method = 'PUT';
        } else {
            sentence.id = null;
        }
        $.ajax({
            method: method,
            url: url,
            data: JSON.stringify(sentence),
            contentType: 'application/json'
        }).done(function (response) {
            $('#sentenceModal').modal('hide');
            if(!sentence.id){
                $('#searchBox').val(response.text);
                search(0, response.text);
            }
        }).fail(function () {
            alert("update fail");
        });
    }

    function reload() {
        saveHistory = false;
        init();
    }

    function onSuccess(response) {
        setTableContent(response);
        paginate(response, renderPagination);
        if (saveHistory) {
            if (getText()) {
                window.history.pushState(
                    {page: response.pageable.pageNumber},
                    'Danh sách câu tìm kiếm',
                    '/admin/sentences/page/' + (response.pageable.pageNumber + 1) + "/?search=" + getText());
            } else {
                window.history.pushState(
                    {page: response.pageable.pageNumber},
                    'Danh sách câu',
                    '/admin/sentences/page/' + (response.pageable.pageNumber + 1) + "/");
            }
        }
    }

    function onError(response) {
        console.error(response);
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

    function setTableContent(page) {
        var content = page.content;
        var pageable = page.pageable;

        removeAllChild(tbody);
        if (content.length) {
            for (var i = 0; i < content.length; i++) {
                var row = document.createElement("tr");
                addColumn(row, function () {
                    return createColumn(pageable.offset + 1 + i)
                });
                addColumn(row, function () {
                    return createColumn(content[i].text);
                });
                addColumn(row, function () {
                    return createColumn(content[i].ipa);
                });
                addColumn(row, function () {
                    return createColumn(content[i].meaning);
                });
                addColumn(row, function () {
                    var audio = document.createElement('td');
                    audio.classList.add('text-center');
                    if (content[i].audioPath) {
                        var link = document.createElement('a');
                        var icon = document.createElement('i');
                        icon.classList.add('fa', 'fa-volume-up');
                        link.appendChild(icon);
                        link.setAttribute('href', url + '/' + content[i].id + '/audio');
                        link.addEventListener('click', function (event) {
                            event.preventDefault();
                            playAudio(event);
                        });
                        audio.appendChild(link);
                    }
                    return audio;
                });
                addColumn(row, function () {
                    var actions = document.createElement("td");
                    var editBtn = document.createElement("button");
                    var icon = document.createElement('i');
                    icon.classList.add('fa', 'fa-pencil');
                    editBtn.appendChild(icon);
                    editBtn.dataset.sentenceId = content[i].id;
                    editBtn.classList.add('btn', 'btn-warning', 'btn-sm');
                    editBtn.addEventListener('click', function (event) {
                        var id = $(this).data("sentenceId");
                        var $modal = $('#sentenceModal').modal('show');
                        $.ajax({
                            method: 'GET',
                            url: '/api/v1/sentences/' + id,
                            dataType: 'json'
                        }).done(function (data) {
                            $modal.find('#sentenceModalTitle').text('Sửa câu');
                            $modal.find('#id').val(data.id);
                            $modal.find('#text').val(data.text);
                            $modal.find('#ipa').val(data.ipa);
                            $modal.find('#meaning').val(data.meaning);
                            $modal.find('#audioPath').val(data.audioPath);
                        }).fail(function () {

                        });
                    });
                    actions.appendChild(editBtn);
                    return actions;
                });
                addRow(row);
            }
        } else {
            var row = document.createElement("tr");
            addColumn(row, function () {
                var td = createColumn("Không có dữ liệu");
                td.setAttribute('colspan', '6');
                return td;
            });
            addRow(row);
        }


        function createColumn(content) {
            var td = document.createElement("td");
            td.textContent = content;
            return td;
        }

        function addColumn(row, column) {
            row.appendChild(column());
        }

        function addRow(row) {
            tbody.appendChild(row);
        }

    }

    function paginate(page, renderPagination) {
        removeAllChild(pagination);
        var pageable = page.pageable;
        var totalPages = page.totalPages;
        var currentPage = pageable.pageNumber;
        var start = 0;
        var range = 5;
        var i = Math.min(totalPages + start - range, Math.max(start, currentPage - (range / 2 | 0)));
        var end = i + range;
        if (i < 0)
            i = 0;
        if (end) {
            renderPagination(i, end, page);
        }
    }

    function renderPagination(i, end, page) {
        var pageable = page.pageable;
        var currentPage = pageable.pageNumber;

        addButton(0, 'First', function (pageItem) {
            if (page.first) {
                pageItem.classList.add('disabled');
            }
        });

        addButton(currentPage - 1, 'Previous', function (pageItem) {
            if (currentPage - 1 < 0) {
                pageItem.classList.add('disabled');
            }
        });

        while (i < end) {
            addButton(i, i + 1, function (pageItem) {
                if (i === currentPage) {
                    pageItem.classList.add('active');
                }
            });
            i++;
        }

        addButton(currentPage + 1, 'Next', function (pageItem) {
            if (currentPage + 1 > page.totalPages - 1) {
                pageItem.classList.add('disabled');
            }
        });

        addButton(page.totalPages - 1, 'Last', function (pageItem) {
            if (page.last) {
                pageItem.classList.add('disabled');
            }
        });

        function addButton(pageIndex, pageLabel, addClassToPageItem) {
            var pageButton = document.createElement('button');
            pageButton.dataset.page = pageIndex;
            pageButton.textContent = pageLabel;
            pageButton.addEventListener('click', function (event) {
                event.preventDefault();
                setPage(+event.target.dataset.page);
            });
            pageButton.classList.add('page-link');
            var pageItem = document.createElement('li');
            pageItem.classList.add('page-item');
            addClassToPageItem(pageItem);
            pageItem.appendChild(pageButton);
            pagination.appendChild(pageItem);
        }
    }

    function removeAllChild(element) {
        while (element.firstChild) {
            element.removeChild(element.firstChild);
        }
    }

    function setPage(page) {
        if (this.page != page) {
            this.page = page;
            saveHistory = true;
            if (getText()) {
                search(page, getText());
            } else {
                getSentences(this.page);
            }
        }
    }

})
;
