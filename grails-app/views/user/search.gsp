<%--
  Created by IntelliJ IDEA.
  User: 647
  Date: 2020/4/27
  Time: 上午 10:06
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search Hubbub</title>
    <meta name="layout" content="main"/> <!--Uses the same layout as the scaffolding for consistency.-->
</head>

<body>
<formset>
    <legend>Search for Friends</legend>
    <g:form action="results">
        <label for="loginId">Login ID</label>
        <g:textField name="loginId" />
        <g:submitButton name="search" value="Search"/>
    </g:form>
</formset>
</body>
</html>