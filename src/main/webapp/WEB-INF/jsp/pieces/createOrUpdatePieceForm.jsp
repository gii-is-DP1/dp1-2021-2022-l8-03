<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="piece">
    <h2>
        <c:if test="${piece['new']}">New </c:if> piece
    </h2>
    <form:form modelAttribute="piece" class="form-horizontal" id="add-piece-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Row" name="tile.rowIndex"/>
            <petclinic:inputField label="Column" name="tile.columnIndex"/> 
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${piece['new']}">
                        <button class="btn btn-default" type="submit">Add piece</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update piece</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
