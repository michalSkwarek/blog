<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="post">
    <c:if test="${post.publishedDate != null}">
        <p class="date">published: ${post.publishedDate}</p>
    </c:if>
    <a class="btn btn-default" href="/post/${post.id}/edit"><span class="glyphicon glyphicon-pencil"></span></a>
    <h1>${post.title}</h1>
    <p>${post.text}</p>
</div>