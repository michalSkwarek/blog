<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="post">
    <c:if test="${post.publishedDate != null}">
        <div class="date">
            <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${post.publishedDate}"/>
        </div>
    </c:if>
    <c:if test="${post.publishedDate == null}">
        <a class="btn btn-default" href="/post/${post.id}/publish">Publish</a>
    </c:if>

    <security:authorize access="hasRole('ROLE_ADMIN')">
        <a class="btn btn-default" href="/post/${post.id}/edit"><span
                class="glyphicon glyphicon-pencil"></span></a>
        <a class="btn btn-default" href="/post/${post.id}/remove"><span
                class="glyphicon glyphicon-remove"></span></a>
    </security:authorize>

    <h1>${post.title}</h1>
    <p>${post.text}</p>
</div>

<hr/>
<a class="btn btn-default" href="/post/${post.id}/comment">Add comment</a>

<c:if test="${empty comments}">
    <p>No comments here yet :(</p>
</c:if>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:forEach items="${comments}" var="comment">
        <div class="comment">
            <div class="date">
                <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${comment.createdDate}"/>
                <c:if test="${comment.approvedComment == false}">
                    <a class="btn btn-default" href="/comment/${comment.id}/remove"><span
                            class="glyphicon glyphicon-remove"></span></a>
                    <a class="btn btn-default" href="/comment/${comment.id}/approve"><span
                            class="glyphicon glyphicon-ok"></span></a>
                </c:if>
            </div>
            <strong>${comment.author}</strong>
            <p>${comment.text}</p>
        </div>
    </c:forEach>
</security:authorize>

<security:authorize access="!hasRole('ROLE_ADMIN')">
    <c:forEach items="${comments}" var="comment">
        <c:if test="${comment.approvedComment == true}">
            <div class="comment">
                <div class="date">
                    <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${comment.createdDate}"/>
                </div>
                <strong>${comment.author}</strong>
                <p>${comment.text}</p>
            </div>
        </c:if>
    </c:forEach>
</security:authorize>