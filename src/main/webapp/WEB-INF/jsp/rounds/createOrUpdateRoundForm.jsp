<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rounds">
    <h2>
        <c:if test="${round['new']}">New </c:if> round
    </h2>
    <form:form modelAttribute="round" class="form-horizontal" id="add-round-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Duration" name="duration"/>
            <petclinic:inputField label="Rapids" name="rapids"/>
            <petclinic:inputField label="Whirlpools" name="whirlpools"/>
            <petclinic:inputField label="numPlayers" name="num_players"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${round['new']}">
                        <button class="btn btn-default" type="submit">Add round</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update round</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
