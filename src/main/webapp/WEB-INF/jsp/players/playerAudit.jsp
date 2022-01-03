<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Auditing ${player.user.username} data</h2>

    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Rev</th>
            <th style="width: 150px;">Rev Type</th>
            <th style="width: 200px;">First Name</th>
            <th style="width: 200px;">Last Name</th>
            <th style="width: 200px;">Email</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${auditedData}" var="data">
            <tr>
                <td>
                    <c:out value="${data[1]}"/>
                </td>
                <td>
                    <c:out value="${data[2]}"/>
                </td>
                <td>
                    <c:out value="${data[3]}"/>
                </td>
                <td>
                    <c:out value="${data[4]}"/>
                </td>
                <td>
                    <c:out value="${data[5]}"/>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
