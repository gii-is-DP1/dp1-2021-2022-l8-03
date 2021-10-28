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
            <th>Duration</th>
            <th>Rapids</th>
            <th>Whirlpools</th>
            <th>numPlayers</th>
            <th style="width: 360px">start</th>
            <th style="width: 360px">end</th>
            <th style="width: 360px">turnStart</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rounds}" var="round">
            <tr>
                <td>
                	<c:out value="${round.duration}"/>
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
                <td>
                    <c:out value="${round.match_start}"/>
                </td>
                <td>
                    <c:out value="${round.match_end}"/>
                </td>
                <td>
                    <c:out value="${round.turn_start}"/>
                </td>   
                <td>
                	<spring:url value="/rounds/{roundId}/edit" var="roundUrl">
                        <spring:param name="roundId" value="${round.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(roundUrl)}" class="btn btn-default" type="submit">Update round</href></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
