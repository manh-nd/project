$(document).ready(function () {
    var tbody = document.querySelector("#sentence-table > tbody");
    var pagination = document.querySelector("#pagination-navigator");
    var audio = document.querySelector("#audioPlayer");
    var url = '/api/v1/sentences';
    var size = 20;
    var page = 0;
    var sort = 'text,asc';
    var playing = false;
    var pause = true;
    var isSaveHistory = true;

    audio.playing = function () {
        console.log("playing...");
        playing = true;
        pause = false;
    };

    audio.pause = function () {
        console.log("paused");
        pause = true;
        playing = false;
    };

    init();

    $("#searchBtn").on('click', function (event) {
        var text = $("#searchBox").val();
        isSaveHistory = true;
        search(0, text);
    });

    $("#searchBox").on('keyup', function (event) {
        var text = $(this).val();
        if (event.keyCode === 13) {
            isSaveHistory = true;
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
            isSaveHistory = false;
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
            if (!sentence.id) {
                $('#searchBox').val(response.text);
                search(0, response.text);
            } else {
                reload();
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
        paginate(pagination, response, setPage);
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
                        td.classList.add('text-center');
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
                            td.appendChild(link);
                        }
                    }),
                    createColumn(function(td){
                        var button = document.createElement("button");
                        var icon = document.createElement('i');
                        icon.classList.add('fa', 'fa-pencil');
                        button.appendChild(icon);
                        button.dataset.sentenceId = content[i].id;
                        button.classList.add('btn', 'btn-warning', 'btn-sm');
                        button.addEventListener('click', function (event) {
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
                        td.appendChild(button);
                    })
                ];
                createRow(tbody, columns);
            }
        } else {
            var row = document.createElement("tr");
            createColumn(row, function () {
                var td = createColumn("Không có dữ liệu");
                td.setAttribute('colspan', '6');
                return td;
            });
            createRow(tbody, row);
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

});
