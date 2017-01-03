<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>New post</h1>
<form method="post" class="post-form">
    <p>Title:</p>
    <input type="text" name="title"/>

    <p>Text:</p>
    <textarea name="text" rows="10"></textarea>
    <br />

    <button type="submit" class="save btn btn-default">Save</button>
</form>