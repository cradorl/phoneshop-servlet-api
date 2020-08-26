<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>${label}<span style="color: red"></span></td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name=${name} value="${not empty error ? param[name]:""}"/>
        <c:if test="${not empty error}">
            <div class="error">
                    ${error}
            </div>
        </c:if>
    </td>
</tr>
