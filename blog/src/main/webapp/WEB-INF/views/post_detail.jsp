<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="post">
    <c:if test="${post.publishedDate != null}">
        <p class="date">published: ${post.publishedDate}</p>
    </c:if>
    <h1>${post.title}</h1>
    <p>${post.text}</p>
</div>