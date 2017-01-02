<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All posts</title>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <link href="//fonts.googleapis.com/css?family=Lobster&subset=latin,latin-ext" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="../../resources/css/blog.css?version=1" type="text/css"/>
</head>
<body>
<div class="page-header">
    <h1><a href="${pageContext.request.contextPath}/">Java Blog</a></h1>
</div>
<div class="content container">
    <div class="row">
        <div class="col-md-8">
            <c:forEach items="${posts}" var="post">
                <div class="post">
                    <p class="date">published: ${post.publishedDate}</p>
                    <h1><a href="">${post.title}</a></h1>
                    <p>${post.text}</p>
                </div>
            </c:forEach>
        </div>
    </div>
</div>