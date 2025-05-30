<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
    <acme:input-moment code="assistance-agent.tracking-log.form.label.creationMoment" path="creationMoment" readonly="true"/>
    <acme:input-double code="assistance-agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage"/>
    <acme:input-textarea code="assistance-agent.tracking-log.form.label.resolution" path="resolution"/>
    <acme:input-textbox code="assistance-agent.tracking-log.form.label.stepUndergoing" path="stepUndergoing"/>
    <jstl:choose>
    	<jstl:when test="${claimDraftMode == true || _command == 'create'}">
        	<acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statuses}"/>
    	</jstl:when>
    	<jstl:otherwise>
        	<acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statuses}" readonly="true"/>
    	</jstl:otherwise>
	</jstl:choose>	

	<jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
	    	<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update?id=${id}"/>
	        <acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete?id=${id}"/>
	        <jstl:if test = "${claimDraftMode == false}">
				<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish?id=${id}"/>
			</jstl:if>
	    </jstl:when>
	    <jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>