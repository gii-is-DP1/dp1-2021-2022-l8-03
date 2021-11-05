<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pieces">
    <h2>Pieces</h2>

    <table id="piecesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Salmon number</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pieces}" var="piece">
            <tr>
                <td>
                    <c:out value="${piece.numSalmon}"/>
                </td>            
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
