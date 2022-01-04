<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rounds">
    <h2>Waiting for players...</h2>

    <table id="rounds" class="table table-striped">
        <thead>
        	<tr>
	           <th>Players</th>  	
        	</tr>
        </thead>
    
        <tbody>
   		 	<c:forEach items="${round.players}" var="player">
   		 		<tr>
	               	<td>
	               		<c:out value="${player.user.username}"/>
	               	</td>
	            </tr>
            </c:forEach>
            
        </tbody>
    </table>
    <td>
    	<c:if test = "${permission}">
		     <spring:url value="/rounds/{roundId}" var="roundUrl">
		     <spring:param name="roundId" value="${round.id}"/>
		     </spring:url>
		     <a href="${fn:escapeXml(roundUrl)}" class="btn btn-default" type="submit">Start game</href></a>
	     </c:if>
    </td>
      
</petclinic:layout>