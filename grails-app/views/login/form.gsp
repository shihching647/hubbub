<%--
  Created by IntelliJ IDEA.
  User: 647
  Date: 2020/5/10
  Time: 下午 08:43
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name = "layout" content="main">
</head>

<body>
    <h1>Please Sign In</h1>
    <g:if test="${flash.message}">
        <div class="errors">
            ${flash.message}
        </div>
    </g:if>
    <g:form action="signIn">
        <div class = "loginId">
            <label for="loginId">Login ID</label>
            <g:textField name="loginId" required="required" value="${loginId}"/>
        </div>

        <div class = "password">
            <label for="password">Password</label>
            <g:passwordField name="password" required="required"/>
        </div>
        <div class = "submit">
            <g:submitButton name="signIn" value="Sign In"/>
        </div>

    </g:form>
</body>
</html>