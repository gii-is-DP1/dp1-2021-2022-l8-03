<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="game" tagdir="/WEB-INF/tags" %>

<game:layout pageName="rounds">
    
    <p>	
    <h2><c:out value="${now}"/></h2>

    <div class="row">
        <div class="col-md-12">
            <game:board salmonBoard="${salmonBoard}"/>
             <c:forEach items="${salmonBoard.round.tiles}" var="tile">
            	<game:tile size="130" tile="${tile}"/>
            </c:forEach> 
            <c:forEach items="${salmonBoard.round.pieces}" var="piece">
            	<game:piece size="130" piece="${piece}"/>
            </c:forEach>

        </div>
    </div>
</game:layout>