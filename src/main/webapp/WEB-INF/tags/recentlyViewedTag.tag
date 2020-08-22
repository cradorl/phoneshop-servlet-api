<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="recentProducts" required="true" type="java.util.concurrent.ConcurrentLinkedDeque" %>

<c:if test="${not empty recentProducts}">
    <br>
    <h3>Recently Viewed</h3>
    <table>
        <thead>
        <c:forEach var="product" items="${recentProducts}">
            <th>
            <td align="center">
                <img src="${product.imageUrl}">
                <br>
                <c:url value="/products/${product.id}" var="productPage"/>
                <a href="${productPage}">${product.description}</a>
                <br>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
            </th>
        </c:forEach>
        </thead>
    </table>
</c:if>
