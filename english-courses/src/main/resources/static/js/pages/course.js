$(document).ready(function () {
    var tableBody = document.getElementById("table-body");
    var pagination = document.getElementById("pagination-navigator");
    var alertContainer = document.getElementById("alert-container");
    var modalAlertContainer = document.getElementById("modal-alert");
    var lessonAlertContainer = document.getElementById('lesson-alert');

    var $id = $('#id');
    var $name = $('#name');
    var $description = $('#description');
    var $orderNumber = $('#orderNumber');

    var $courseModal = $('#course-modal');
    var $lessonModal = $('#lesson-modal');
    var $deleteModal = $('#delete-modal');

    var $createButton = $('#create-button');
    var $saveButton = $('#save-button');
    var $saveLessonButton = $('#save-lesson-button');
    var $deleteButton =  $('#delete-button');

    var page = 0;
    var size = 20;

    init();

    $courseModal.on('shown.bs.modal', function () {
        $name.trigger('focus');
    });

    $lessonModal.on('shown.bs.modal', function () {
        $orderNumber.trigger('focus');
    });

    $createButton.on('click', function () {
        $courseModal.find('.modal-title').text('Thêm mới khóa học');
        $courseModal.find('#id').val(null);
        $courseModal.find('#name').val(null);
        $courseModal.find('#description').val(null);
        $courseModal.modal('show');
    });

    $saveButton.on('click', function () {
        saveCourse();
    });

    $saveLessonButton.on('click', function () {
        var courseId = $lessonModal.find('#courseId').val();
        var orderNumber = $lessonModal.find('#orderNumber').val();
        var lesson = {
            courseId: courseId,
            orderNumber: orderNumber
        };
        saveLesson(lesson);
    });

    $deleteButton.on('click', function () {
        deleteCourse($(this).val());
    });

    function init() {
        getCourses();
    }

    function getCourses() {
        get('/api/v1/courses', {page: page, size: size}, function (data) {
            renderTableBody(data);
            paginate(pagination, data, setPage);
        }, function () {
            console.log("error");
        });
    }

    function saveCourse() {
        var id = $courseModal.find('#id').val();
        var name = $courseModal.find('#name').val();
        var description = $courseModal.find('#description').val();
        var course = {
            id: id ? id : null,
            name: name,
            description: description
        };
        var settings = {
            url: course.id ? '/api/v1/courses/' + id : '/api/v1/courses',
            method: course.id ? 'PUT' : 'POST',
            data: JSON.stringify(course),
            contentType: 'application/json',
            dataType: 'json',
            success: function () {
                $courseModal.modal('hide');
                getCourses();
                if (id) {
                    showAlert(alertContainer, 'success', 'Sửa khóa học thành công.')
                } else {
                    showAlert(alertContainer, 'success', 'Thêm mới khóa học thành công.');
                }
            },
            error: function (res) {
                var responseJSON = res.responseJSON;
                if (responseJSON.errors && responseJSON.errors.name[0] && responseJSON.errors.name[0].message) {
                    showAlert(modalAlertContainer, 'danger', errors.name[0].message);
                    $name.focus();
                }
                if(responseJSON.key && responseJSON.key === 'course.name'){
                    showAlert(modalAlertContainer, 'danger', 'Trùng tên khóa học');
                    $name.focus();
                }
            }
        };
        $.ajax(settings);
    }

    function deleteCourse(id) {
        var settings = {
            url: '/api/v1/courses/' + id,
            method: 'DELETE',
            success: function () {
                showAlert(alertContainer, 'success', 'Xóa khóa học thành công.');
                $deleteModal.modal('hide');
                getCourses();
            },
            error: function () {
                showAlert(alertContainer, 'danger', 'Lỗi hệ thống.');
                $deleteModal.modal('hide');
            }
        };
        $.ajax(settings);
    }

    function saveLesson(lesson) {
        var settings = {
            url: '/api/v1/lessons',
            'method': 'POST',
            data: JSON.stringify(lesson),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {
                $lessonModal.modal('hide');
                showAlert(alertContainer, 'success', 'Thêm mới bài học vào khóa học ' + data.courseName + ' thành công.');
            },
            error: function (res) {
                var headers = parseHttpHeaders(res.getAllResponseHeaders());
                if (headers['entity-exists'] === 'Lesson') {
                    showAlert(lessonAlertContainer, 'danger', 'Thêm khóa học thất bại vì lesson đã tồn tại.');
                }
            }
        };
        $.ajax(settings);
    }


    function renderTableBody(page) {
        var content = page.content;
        var pageable = page.pageable;
        removeAllChildren(tableBody);
        var columns = [];
        if (content.length) {
            for (var i = 0; i < content.length; i++) {
                columns = [
                    createColumn(function (td) {
                        td.textContent = pageable.offset + i + 1;
                    }),
                    createColumn(function (td) {
                        var a = document.createElement('a');
                        a.href = '/admin/management/courses/' + content[i].id + '/lessons';
                        a.textContent = content[i].name;
                        td.appendChild(a);
                    }),
                    createColumn(function (td) {
                        td.textContent = content[i].description;
                    }),
                    createColumn(function (td) {
                        var createLessonButton = createButton(function (button) {
                            button.dataset.id = content[i].id;
                            button.dataset.name = content[i].name;
                            button.classList.add('btn', 'btn-sm', 'btn-primary', 'mr-1');
                            button.appendChild(createIcon(['fa', 'fa-plus']));
                            button.dataset.toggle = 'tooltip';
                            button.setAttribute('title', 'Thêm mới bài học');
                            button.addEventListener('click', onClickCreateLessonButton);
                        });
                        td.appendChild(createLessonButton);
                        var updateCourseButton = createButton(function (button) {
                            button.dataset.id = content[i].id;
                            button.dataset.name = content[i].name;
                            button.dataset.description = content[i].description ? content[i].description : '';
                            button.classList.add('btn', 'btn-warning', 'btn-sm', 'update-button', 'mr-1');
                            button.setAttribute('title', 'Sửa khóa học');
                            button.appendChild(createIcon(['fa', 'fa-pencil']));
                            button.dataset.toggle = 'tooltip';
                            button.setAttribute('title', 'Sửa khóa học');
                            button.addEventListener('click', onClickUpdateCourseButton);
                        });
                        td.appendChild(updateCourseButton);
                        var deleteCourseButton = createButton(function (button) {
                            button.dataset.id = content[i].id;
                            button.dataset.name = content[i].name;
                            button.classList.add('btn', 'btn-sm', 'btn-danger', 'delete-button');
                            button.appendChild(createIcon(['fa', 'fa-close']));
                            button.dataset.toggle = 'tooltip';
                            button.setAttribute('title', 'Xóa khóa học');
                            button.addEventListener('click', onClickDeleteCourseButton);
                        });
                        td.appendChild(deleteCourseButton);
                    })
                ];
                createRow(tableBody, columns);
            }
            $('[data-toggle="tooltip"]').tooltip();
        } else {
            columns = [
                createColumn(function (td) {
                    td.setAttribute("colspan", 4);
                    td.textContent = "Không có bản ghi.";
                })
            ];
            createRow(tableBody, columns);
        }
    }

    function onClickCreateLessonButton() {
        var element = $(this);
        var courseId = element.data('id');
        $lessonModal.find('.modal-title').text('Tạo bài học');
        $lessonModal.find('#courseId').val(element.data('id'));
        $lessonModal.find('#courseName').val(element.data('name'));
        $.ajax({
            url: '/api/v1/courses/' + courseId + '/lessons/order-number',
            method: 'GET',
            data: {
                next: true
            },
            dataType: 'json',
            success: function (next) {
                $orderNumber.val(next);
                $lessonModal.modal('show');
            },
            error: function () {
                showAlert(alertContainer, 'danger', 'Lỗi hệ thống.');
            }
        });
    }

    function onClickUpdateCourseButton() {
        var element = $(this);
        $courseModal.find('.modal-title').text('Sửa khóa học');
        $courseModal.find('#id').val(element.data('id'));
        $courseModal.find('#name').val(element.data('name'));
        $courseModal.find('#description').val(element.data('description'));
        $courseModal.modal('show');
    }

    function onClickDeleteCourseButton() {
        var element = $(this);
        $deleteButton.val(element.data('id'));
        $deleteModal.find('.modal-title').text('Thông báo hệ thống');
        $deleteModal.find('.modal-body').html('Bạn có muốn xóa khóa học ' + '<strong>[' + element.data('name') + ']</strong> không?');
        $deleteModal.modal('show');
    }

    function setPage(page) {
        if (this.page !== page) {
            this.page = page;
        }
    }

});