<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="game" tagdir="/WEB-INF/tags" %>

<game:layout pageName="rounds">
    
    <p>	
    <h2><c:out value="${now}"/></h2>

	<div class="row" id="piecesPlayer">
		<table id="pieces" class="table table-striped">
        <thead>
        <h2>Your Pieces</h2>
        <tr>
            <th>Piece</th>
            <th>Row</th>
            <th>Column</th>
            <th>Movement</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
	        <tbody>

		        <c:forEach items="${player.pieces}" var="piece" varStatus="status">
		            <tr>
		                <td>
		                    <c:out value= "${status.count}"/>
		                </td>
		                <td>
		                    <c:out value="${piece.tile.rowIndex}"/>
		                </td>
		                <td>
		                    <c:out value="${piece.tile.columnIndex}"/>
		                </td>

		                <td>   
                			<spring:url value="/piece/{pieceId}/edit" var="pieceUrl">
                			<spring:param name="pieceId" value="${piece.id}"/>
                			</spring:url>
                			<a href="${fn:escapeXml(pieceUrl)}" class="btn btn-default" type="submit">Move</href></a>
		                </td>
		                
		            </tr>
		        </c:forEach>
	        </tbody>
    	</table>
	</div>

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