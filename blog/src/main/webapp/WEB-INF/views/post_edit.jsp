<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>New post</h1>
<form method="post" class="post-form">
    <input type="hidden" name="author" value="${pageContext.request.userPrincipal.name}">

    <p>Title:</p>
    <input type="text" name="title"/>

    <p>Text:</p>
    <textarea name="text" rows="10"></textarea>

    <button type="submit" class="save btn btn-default">Save</button>
</form>