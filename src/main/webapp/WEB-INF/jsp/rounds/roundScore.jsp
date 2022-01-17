<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rounds">
    <h2>Round scores</h2>

    <table id="scores" class="table table-striped">
        <thead>
        <tr>
            <th>Player</th>
            <th>Score</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${scoreList}" var="score">
            <tr>
	           <td>
	           		<c:out value="${score.player.user.username}"/>
	           </td>
	           <td>
	           		<c:out value="${score.value}"/>
	           </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>