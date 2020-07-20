<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="sort" required="true" %>

<a href="?sort=${sort}&order=${order}&query=${param.query}">asc</a>
