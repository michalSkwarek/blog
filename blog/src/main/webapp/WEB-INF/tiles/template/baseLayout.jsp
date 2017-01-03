<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>
<head>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <link href="//fonts.googleapis.com/css?family=Lobster&subset=latin,latin-ext" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/resources/css/blog.css?version=1" type="text/css"/>
    <title><tiles:insertAttribute name="title"/></title>
</head>
<body>
<section>
    <tiles:insertAttribute name="header"/>

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
