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
        <c:if test="${esFinished}">
            <th>Duration</th>
        </c:if>
            <th>Rapids</th>
            <th>Whirlpools</th>
            <th>NumPlayers</th>
            <th>RoundCreator</th>
            
        <c:if test="${esFinished}">
            <th style="width: 360px">start</th>
        </c:if>
        
        <c:if test="${esFinished}">
            <th style="width: 360px">end</th>
        </c:if>
        
        <c:if test="${esFinished}">
            <th style="width: 360px">turnStart</th>
        </c:if>

            <th> 
                <spring:url value="/rounds/new" var="roundUrl"></spring:url>
                <a href="${fn:escapeXml(roundUrl)}" class="btn btn-default" type="submit">Create round</href></a>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rounds}" var="round">
            <tr>
            
            <c:if test="${esFinished}">
                <td>
                    <c:out value="${round.duration}"/>
                </td>
            </c:if>
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
                    <c:out value="${round.player.user.username}"/>
                </td>
           <c:if test="${esFinished}">  
                <td>
                    <c:out value="${round.match_start}"/>
                </td>
           </c:if>     
           <c:if test="${esFinished}">
                <td>
                    <c:out value="${round.match_end}"/>
                </td>
           </c:if>     
           <c:if test="${esFinished}">
                <td>
                    <c:out value="${round.turn_start}"/>
                </td>   
           </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>