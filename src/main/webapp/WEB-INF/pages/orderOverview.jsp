<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="quantity">
                Quantity
            </td>
            <td class="price">
                Price
            </td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile" src="${item.product.imageUrl}">
                </td>
                <td>
                    <a href="products/${item.product.id}">
                            ${item.product.description}
                    </a>
                </td>
                <td class="quantity">
                    <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        ${item.quantity}
                </td>
                <td class="price">
                    <a href="products/priceHistory/${item.product.id}">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Subtotal:</td>
            <td class="price">
                <p>
                    <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${order.currency}"/>
                </p>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Delivery cost:</td>
            <td class="price">
                <p>
                    <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                      currencySymbol="${order.currency}"/>
                </p>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Total cost:</td>
            <td class="price">
                <p>
                    <fmt:formatNumber value="${order.totalCost}" type="currency"
                                      currencySymbol="${order.currency}"/>
                </p>
            </td>
        </tr>
    </table>
    </table>
    <h2>Your details</h2>
    <table>
        <tags:orderOverviewRow name="firstName" label="First name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"></tags:orderOverviewRow>
        <tr>
            <td>Payment method</td>
            <td>
                    ${order.paymentMethod}
            </td>
        </tr>
    </table>

    </form>
</tags:master>
