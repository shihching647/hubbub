<%--
  Created by IntelliJ IDEA.
  User: 647
  Date: 2020/5/10
  Time: 下午 04:29
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
</head>

<body>
    <div class = "profilePic">
        <g:if test = "${profile.photo}">
            <img src = "${createLink(controller: 'image', action: 'renderImage', id: profile.user.loginId)}" style="width: 300px;">
        </g:if>
        <p>Profile for <strong>${profile.fullName}</strong></p>
        <p>Bio: ${profile.bio}</p>
    </div>
</body>
</html>