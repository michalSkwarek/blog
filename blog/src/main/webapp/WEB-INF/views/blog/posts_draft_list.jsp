<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${posts}" var="post">
    <div class="post">
        <p class="date">created: ${post.createdDate}</p>
        <h1><a href="/post/${post.id}">${post.title}</a></h1>
        <p>${post.text}</p>
    </div>
</c:forEach>