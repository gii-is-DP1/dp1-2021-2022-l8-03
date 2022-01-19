<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="piece">
    <h2>
        Moving piece
    </h2>
    <form:form modelAttribute="movementTypeWrapper" class="form-horizontal" id="add-piece-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Row" name="piece.tile.rowIndex"/>
            <petclinic:inputField label="Column" name="piece.tile.columnIndex"/> 
        </div>
        <label for="movementType">Movement Type</label> 
        	<input type="radio" name="movementType" value="false" checked>Swim
        	<input type="radio" name="movementType" value="true">Jump
        <div class="form-group"> 
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-default" type="submit">Move</button>
                  
                <spring:url value="/rounds/{roundId}" var="roundUrl">
               		<spring:param name="roundId" value="${round.id}"/>
               	</spring:url>
               	<a href="${fn:escapeXml(roundUrl)}" class="btn btn-default">Back</href></a>
            </div>
        </div>
    </form:form>
</petclinic:layout>
