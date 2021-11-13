<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rapidsTiles">
    <h2>RapidTiles</h2>

    <table id="rapidTilesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Orientation</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rapidTiles}" var="rapidTile">
            <tr>
                <td>
                    <c:out value="${rapidTile.orientation}"/>
                </td>          
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>