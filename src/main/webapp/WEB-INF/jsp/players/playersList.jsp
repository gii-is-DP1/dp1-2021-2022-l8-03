<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Players</h2>

    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Username</th>
            <th style="width: 150px;">First name</th>
            <th style="width: 200px;">Last name</th>
            <th style="width: 200px;">Email</th>
            <th style="width: 200px;"></th>
            <th style="width: 200px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections.content}" var="player">
            <tr>
                <td>
                    <spring:url value="/players/{playerId}" var="playerUrl">
                        <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(playerUrl)}"><c:out value="${player.user.username}"/></a>
                </td>
                <td>
                    <c:out value="${player.firstName}"/>
                </td>
                <td>
                    <c:out value="${player.lastName}"/>
                </td>
                <td>
                    <c:out value="${player.email}"/>
                </td>
                <td>
                	<spring:url value="/players/{playerId}/edit" var="playerUrl">
                        <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(playerUrl)}" class="btn btn-default" type="submit">Update player</href></a>
                </td>
                <td>
                	<spring:url value="/players/delete/{playerId}" var="playerUrl">
                        <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(playerUrl)}" class="btn btn-default" type="submit">Delete player</href></a>
                </td>              
            </tr>
        </c:forEach>

			<c:if test="${esPrimera}">
	                    <spring:url value="/players?page={pageNumber}&size=6" var="previousUrl">
	                        <spring:param name="pageNumber" value="${selections.number-1}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(previousUrl)}" class="btn btn-default" type="submit">Previous page</href></a>
	        </c:if>      
	        <c:if test="${esUltima}">            
	                    <spring:url value="/players?page={pageNumber}&size=6" var="nextUrl">
	                        <spring:param name="pageNumber" value="${selections.number+1}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(nextUrl)}" class="btn btn-default" type="submit">Next page</href></a>
	        </c:if>     

    
        </tbody>
    </table>
</petclinic:layout>
