<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rounds">
    <h2>
        <c:if test="${round['new']}">New </c:if> 
        <c:if test="${!round['new']}">Update </c:if> round
    </h2>
    <form:form modelAttribute="round" class="form-horizontal" id="add-round-form" action="/rounds">
        <div class="form-group has-feedback">
        	<div class="col-sm-offset-2 col-sm-10">
		        <div class="row-md">
		        	<label>Game mode:
			            <div class="control-group"> 
			            		<label for="whirlpool">Whirlpool</label>
			                    <input type="radio" name="Whirlpool" value="true">
			  					<label for="whirlpool"> Yes</label> 
			  					<input type="radio" name="Whirlpool" value="false" checked>
			  					<label for="whirlpool"> No</label>
			  					<br>
			  					<label for="rapids">Rapids</label> 
			  					<input type="radio" name="rapids" value="true">Yes 
			  					<input type="radio" name="rapids" value="false" checked>No
			            </div>
		        	</label>
		        </div>
		        <div class="row-md">
		 			<label for="num_players">Players number</label>
			        <select label="Players number" name="num_players" id="num_players">
						<option value=2>2</option>
						<option value=3>3</option>
						<option value=4>4</option>
						<option value=5>5</option>
					</select>
				</div>
			</div>
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
