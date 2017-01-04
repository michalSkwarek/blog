<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>New comment</h1>
<form:form modelAttribute="comment" method="post" class="post-form">
    <p>Author:</p>
    <form:errors path="author" />
    <form:input path="author" id="author"/>

    <p>Text:</p>
    <form:errors path="text" />
    <form:input path="text" id="text"/>
    <%--<textarea name="text" rows="10"></textarea>--%>

    <button type="submit" class="save btn btn-default">Send</button>
</form:form>