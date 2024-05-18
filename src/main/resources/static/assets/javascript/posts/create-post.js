var userTags = [], selectedUser = [], searchUserTags = [];
function createPost() {
    let formData = new FormData();
    let content = $('#content').val();

    formData.append('access', $('#access').val());
    formData.append('content', content);
    formData.append('postType', "POST");

    files = files || [];
    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }

    selectedUser.forEach(function(user) {
        formData.append('userTagIds', user.userId);
    });

    let hagTags = content.split(' ').filter(function(word) {
        return word.startsWith('#');
    });
    hagTags = hagTags || [];
    hagTags.forEach(function(tag) {
        formData.append('hagTags', tag);
    });

    $.ajax({
        url: '/api/posts/create',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('Post created successfully:', response);
            clearForm();
        },
        error: function(xhr, status, error) {
            console.error('Error creating post:', error);
        }
    });
}

function clearForm(){
    $('#content').val('');
    $('#access').val('PUBLIC');
    files = [];
    selectedUser = [];
    $('#file-input').val('');
    $('#file-list').empty();
    $('.modal-backdrop').hide();
    $('#exampleModal').hide();
}

function getFriends() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/users/get-user-friends",
        success: function (response) {
            console.log("API Response:", response);
            userTags = response.map(function(user) {
                return {
                    userId: user.userId,
                    nickName: user.nickName,
                    avatar: user.avatar,
                    isSelected: true
                };
            });
            searchUserTags = userTags;
            displayFriendsView();
        },
        error: function (xhr, status, error) {
            console.error("There was a problem with the AJAX request:", error);
        },
    });
}

function displayFriendsView() {
    const listFriends = $("#list-friends");
    listFriends.empty();

    searchUserTags.forEach((user) => {
        if(user.isSelected === true){
            const cardBody = $("<div>").addClass("card-body d-flex pt-4 ps-4 pe-4 pb-0 border-top-xs bor-0")
                .attr("id", "user-card-" + user.userId)
                .click(function() {
                    addUserTag(user);
                });

            const figure = $("<figure>").addClass("avatar me-3");
            const img = $("<img>").attr({
                src: user.avatar,
                alt: "image"
            }).addClass("shadow-sm rounded-circle w45");
            figure.append(img);

            const h4 = $("<h4>").addClass("fw-700 text-grey-900 font-xssss mt-2").text(user.nickName);

            cardBody.append(figure, h4);
            listFriends.append(cardBody);
        }
    });
}

function addUserTag(user) {
    var isUserAlreadySelected = selectedUser.some(function(u) {
        return u.userId === user.userId;
    });

    if (!isUserAlreadySelected) {
        selectedUser.push(user);
        changeStatusUserTag(user.userId, false);
        displayFriendsView();
        addUsersTagDiv(user);
    }
}

function changeStatusUserTag(userId, status){
    userTags = userTags.map(function(u) {
        if (u.userId === userId) {
            u.isSelected = status;
        }
        return u;
    });
}

function addUsersTagDiv(user){
    var userTagItemContainer = $("<div>")
        .addClass("btn-group m-1")
        .attr("id", "user-tag-item-" + user.userId)
        .attr("role", "group")
        .attr("aria-label", "Second group");

    var userTagButton = $("<button>")
        .addClass("btn btn-dark")
        .text(user.nickName);

    var removeButton = $("<button>")
        .addClass("btn btn-dark remove-user-tag")
        .attr("type", "button")
        .attr("data-userId", user.userId)
        .text("x").click(function() {
            var removeUserId = $(this).data('userid');
            $('#user-tag-item-' + removeUserId).remove();
            removeUserById(removeUserId);
            changeStatusUserTag(removeUserId, true);
            displayFriendsView();
        });

    userTagItemContainer.append(userTagButton, removeButton);
    $("#users-tag").append(userTagItemContainer);
}
function removeUserById(userIdToRemove) {
    selectedUser = selectedUser.filter(function(user) {
        return user.userId !== userIdToRemove;
    });
}
$(document).ready(function () {
    getFriends();

    $("#createPost").on("click", function () {
        createPost();
    });

    $("#search-friends").on("input", function () {
       let text = $(this).val().toLowerCase().trim();
       searchUserTags = userTags.filter(function (user) {
           return user.nickName.toLowerCase().includes(text);
       });
       displayFriendsView();
    });

});
