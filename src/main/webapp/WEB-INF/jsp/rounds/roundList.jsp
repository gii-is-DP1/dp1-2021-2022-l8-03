<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rounds">
    <h2>Rounds</h2>

    <table id="rounds" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Duration</th>
            <th style="width: 200px;">Rapids</th>
            <th>Whirlpools</th>
            <th style="width: 120px">numPlayers</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="round">
            <tr>
                <td>
                    <spring:url value="/rounds/{roundId}" var="roundUrl">
                        <spring:param name="roundId" value="${round.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(roundUrl)}"><c:out value="${round.duration}"/></a>
                </td>
                <td>
                    <c:out value="${round.rapids}"/>
                </td>
                <td>
                    <c:out value="${round.whirlpools}"/>
                </td>
                <td>
                    <c:out value="${round.num_players}"/>
                </td>
                
      
<!--
                <td> 
                    <c:out value="${round.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${round.user.password}"/> 
                </td> 
-->
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
