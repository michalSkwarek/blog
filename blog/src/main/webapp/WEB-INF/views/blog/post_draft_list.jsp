<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach items="${posts}" var="post">
    <div class="post">
        <div class="date">
            created: <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${post.createdDate}"/>
        </div>
        <h1><a href="/post/${post.id}">${post.title}</a></h1>
        <p>${post.text}</p>
    </div>
</c:forEach>