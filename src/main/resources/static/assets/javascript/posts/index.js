document.addEventListener('DOMContentLoaded', function() {
    let createPostModal = document.getElementById('exampleModal');
    let tagUsersModal = document.getElementById('tag-friends');

    createPostModal.addEventListener('show.bs.modal', function () {
        let tagUsersModalInstance = bootstrap.Modal.getInstance(tagUsersModal);
        if (tagUsersModalInstance) {
            tagUsersModalInstance.hide();
        }
    });

    tagUsersModal.addEventListener('show.bs.modal', function () {
        let createPostModalInstance = bootstrap.Modal.getInstance(createPostModal);
        if (createPostModalInstance) {
            createPostModalInstance.hide();
        }
    });

    tagUsersModal.addEventListener('hidden.bs.modal', function () {
        let createPostModalInstance = bootstrap.Modal.getInstance(createPostModal);
        createPostModalInstance.show();
    });

});
