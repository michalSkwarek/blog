<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>New post</h1>
<form:form modelAttribute="post" method="post" class="post-form">
    <form:hidden path="id" />

    <p>Title:</p>
    <form:errors path="title" />
    <form:input path="title" id="title"/>

    <p>Text:</p>
    <form:errors path="text" />
    <form:input path="text" id="text"/>
    <%--<textarea name="text" rows="10"></textarea>--%>

    <button type="submit" class="save btn btn-default">Save</button>
</form:form>