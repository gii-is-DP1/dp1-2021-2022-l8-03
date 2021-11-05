<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="scores">
    <h2>Scores</h2>

    <table id="scoresTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Value</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${scores}" var="score">
            <tr>
                <td>
                    <c:out value="${score.value}"/>
                </td>            
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
