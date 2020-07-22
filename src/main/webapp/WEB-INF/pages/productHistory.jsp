<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Price History">
    <h1>Price History</h1>
    <h2>${product.description}</h2>

    <tr>
        <td><a style='font-weight: bold '>Start date</a></td>
        <td><a style='font-weight: bold '>Price</a></td>
    </tr>

    <c:forEach var="productHistory" items="${product.histories}">
        <tr>
            <td>
                    ${productHistory.startDate}
            </td>
            <td>
                <fmt:formatNumber value="${productHistory.price}" type="currency" currencySymbol="${productHistory.currency.symbol}"/>
            </td>
        </tr>
    </c:forEach>
</tags:master>
