<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link1" action="http://www.youtube.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link5" action="https://www.chess.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link2" action="http://www.primevideo.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link4" action="https://www.twitch.tv/"/>
      <acme:menu-suboption code="master.menu.anonymous.favourite-link3" action="https://play.pokemonshowdown.com/"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
				<acme:menu-suboption code="master.menu.administrator.populate-db-forecast" action="/administrator/forecast/perform"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
      <acme:menu-suboption code="master.menu.administrator.dashboard" action="/administrator/administrator-dashboard/show"/>
      <acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.airport" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.airline" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.aircraft" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.service" action="/administrator/service/list"/>
			<acme:menu-suboption code="master.menu.administrator.maintenance-record" action="/administrator/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.administrator.booking" action="/administrator/booking/list"/>
			<acme:menu-separator/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.recommendation" action="/administrator/recommendation/perform"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
		<acme:menu-suboption code="master.menu.manager.flight" action="/manager/flight/list"/>
		<acme:menu-suboption code="master.menu.manager.dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.passenger" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.dashboard" action="/customer/customer-dashboard/show"/>
			<acme:menu-suboption code="master.menu.customer.recommendation" action="/customer/recommendation/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.technician" access="hasRole('Technician')">
			<acme:menu-suboption code="master.menu.technician.dashboard.show" action="/technician/technician-dashboard/show"/>
		    <acme:menu-suboption code="master.menu.technician.tasks.list.mine" action="/technician/task/list?published=false"/>
		    <acme:menu-suboption code="master.menu.technician.tasks.list.published" action="/technician/task/list?published=true"/>
			<acme:menu-suboption code="master.menu.technician.maintenance-record.list.mine" action="/technician/maintenance-record/list?published=false"/>	
			<acme:menu-suboption code="master.menu.technician.maintenance-record.list.published" action="/technician/maintenance-record/list?published=true"/>
			<acme:menu-suboption code="master.menu.technician.course.list" action="/technician/course/list"/>		
		</acme:menu-option>
    
		<acme:menu-option code="master.menu.assistance-agent" access="hasRole('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.dashboard.show" action="/assistance-agent/assistance-agent-dashboard/show"/>
		    <acme:menu-suboption code="master.menu.assistance-agent.claims.list.completed" action="/assistance-agent/claim/list?completed=true"/>
		    <acme:menu-suboption code="master.menu.assistance-agent.claims.list.undergoing" action="/assistance-agent/claim/list?completed=false"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.review" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.review.list" action="/any/review/list"/>
		</acme:menu-option>
      
		<acme:menu-option code="master.menu.review" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.review.list" action="/any/review/list"/>
		</acme:menu-option>
    
		<acme:menu-option code="master.menu.flight-crew-member" access="hasRole('FlightCrewMember')">
		    <acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list.completeLeg" action="/flight-crew-member/flight-assignment/list?completed=true"/>
		    <acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list.incompleteLeg" action="/flight-crew-member/flight-assignment/list?completed=false"/>
    </acme:menu-option>

		<acme:menu-option code="master.menu.flight" access="isAnonymous()">
		<acme:menu-suboption code="master.menu.flight.list" action="/any/flight/list"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.flight" access="isAuthenticated()">
		<acme:menu-suboption code="master.menu.flight.list" action="/any/flight/list"/>
		</acme:menu-option>
			<acme:menu-option code="master.menu.system-configuration" access="hasRealm('Administrator')">
		<acme:menu-suboption code="master.menu.system-configuration.show" action="/administrator/system-configuration/show"/>
		</acme:menu-option>
	
		<acme:menu-option code="master.menu.service" access="isAnonymous()">
		<acme:menu-suboption code="master.menu.service.list" action="/any/service/list"/>
		</acme:menu-option>
		<acme:menu-option code="master.menu.service" access="isAuthenticated()">
		<acme:menu-suboption code="master.menu.service.list" action="/any/service/list"/>
		</acme:menu-option>

	</acme:menu-left>

		
	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/manager/update" access="hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

