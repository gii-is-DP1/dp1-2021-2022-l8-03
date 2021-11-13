<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rockTiles">
    <h2>RockTiles</h2>

    <table id="rockTilesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Row</th>
            <th style="width: 150px;">Column</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rockTiles}" var="rockTile">
            <tr>
                <td>
                    <c:out value="${rockTile.rowIndex}"/>
                </td>   
                <td>
                	<c:out value="${rockTile.columnIndex}"/>
                </td>         
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>>