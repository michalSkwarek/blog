<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <link href="//fonts.googleapis.com/css?family=Lobster&subset=latin,latin-ext" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/resources/css/blog.css?version=1" type="text/css"/>
    <title>Django Girls Blog in Java</title>
</head>
<body>
<section>
    <div class="page-header">
        <security:authorize access="hasRole('ROLE_ADMIN')">
            <a href="/post/new" class="top-menu"><span class="glyphicon glyphicon-plus"></span></a>
            <a href="/drafts" class="top-menu"><span class="glyphicon glyphicon-edit"></span></a>
            <p class="top-menu">Hello ${pageContext.request.userPrincipal.name}
                <small>(<a href="/accounts/logout">Log out</a>)</small>
            </p>
        </security:authorize>
        <security:authorize access="!hasRole('ROLE_ADMIN')">
            <a href="/accounts/login" class="top-menu"><span class="glyphicon glyphicon-lock"></span></a>
        </security:authorize>
        <h1><a href="/">Django Girls Blog in Java</a></h1>
    </div>

    <div class="content container">
        <div class="row">
            <div class="col-md-8">
                <tiles:insertAttribute name="content"/>
            </div>
        </div>
    </div>
</section>
</body>
</html>
