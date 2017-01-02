<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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