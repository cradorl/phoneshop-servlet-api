<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">

    <p></p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>

    <form><label>
        <input name="query" value="">
    </label>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td class="quantity">
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="status">
        <form method="post">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="products/${product.id}">
                            ${product.description}
                    </a>
                </td>

                <td class="quantity">
                    <c:set var="error" value="${errors[product.id]}" />
                    <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity" />
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                    <input type="hidden" name="productId" value="${product.id}" />
                </td>

                <td class="price">
                    <a href="products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button>
                        Add to cart
                    </button>
                </td>
            </tr>
        </form>
        </c:forEach>
    </table>
    <tags:recentlyViewedTag recentProducts="${recentProducts}"/>
</tags:master>
