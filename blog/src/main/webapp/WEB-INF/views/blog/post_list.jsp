<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${posts}" var="post">
    <div class="post">
        <div class="date">
                ${post.publishedDate}
        </div>
        <h1><a href="/post/${post.id}">${post.title}</a></h1>
        <p>${post.text}</p>
        <a href="/post/${post.id}">Comments: ${post.approvedCommentsCounter}</a>
    </div>
</c:forEach>