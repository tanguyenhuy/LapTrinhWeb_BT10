$(document).ready(function() {

    if (window.location.pathname.includes("/user/profile")) {
        $.ajax({
            type: 'GET',
            url: '/users/me',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                if (localStorage.token) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
                }
            },
            success: function(data) {
                $('#profile').html(data.fullName + " (" + data.email + ")");
                if (data.images) {
                    $('#images').attr("src", data.images);
                }
            },
            error: function(e) {
                var json = e.responseText;
                $('#feedback').html("<div class='alert alert-danger'>Sorry, you are not logged in hoặc Token đã hết hạn!</div>");
                alert("Sorry, you are not logged in.");
                window.location.href = "/login";
            }
        });
    }

    $('#Login').click(function() {
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;

        if (!email || !password) {
            alert("Vui lòng nhập đầy đủ Email và Mật khẩu!");
            return;
        }

        var basicInfo = JSON.stringify({
            email: email,
            password: password
        });

        $.ajax({
            type: "POST",
            url: "/auth/login",
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            data: basicInfo,
            success: function(data) {

                localStorage.token = data.token;
                window.location.href = "/user/profile";
            },
            error: function() {
                alert("Login Failed: Sai tài khoản hoặc mật khẩu!");
            }
        });
    });


    $('#Logout').click(function() {
        localStorage.clear(); 
        window.location.href = "/login";
    });
});