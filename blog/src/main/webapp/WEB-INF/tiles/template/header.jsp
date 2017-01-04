<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

