<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search page">

    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>

    <c:if test="${not empty errors}">
        <div class="error">
            Error occurred while placing order
        </div>
    </c:if>

    <h2>Advanced search page</h2>
    <table>
    <tags:searchForm name="code" label="Product code" errors="${errors}"></tags:searchForm>
    <tags:searchForm name="minPrice" label="Min price" errors="${errors}"></tags:searchForm>
    <tags:searchForm name="maxPrice" label="Max Price" errors="${errors}"></tags:searchForm>
    <tags:searchForm name="minStock" label="Min stock" errors="${errors}"></tags:searchForm>
    </table>
    <p>
        <button>Search</button>
    </p>
</tags:master>
