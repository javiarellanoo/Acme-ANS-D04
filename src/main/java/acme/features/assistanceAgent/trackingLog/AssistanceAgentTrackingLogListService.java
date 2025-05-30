
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state
	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int agentId;
		int masterId;
		Claim claim;
		AssistanceAgent agent;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agent = this.repository.findAssistanceAgentById(agentId);
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		status = claim != null && claim.getAssistanceAgent().equals(agent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> tLogs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		tLogs = this.repository.findTrackingLogsByClaim(masterId);

		super.getBuffer().addData(tLogs);
		super.getResponse().addGlobal("masterId", masterId);
	}

	@Override
	public void unbind(final TrackingLog tLog) {
		Dataset dataset;

		dataset = super.unbindObject(tLog, "claim.id", "resolutionPercentage", "status", "lastUpdateMoment", "draftMode", "creationMoment");

		super.addPayload(dataset, tLog, //
			"lastUpdateMoment", "creationMoment", "stepUndergoing", "resolutionPercentage", "status", "resolution", //
			"draftMode", "claim.assistanceAgent.identity.fullName", "claim.leg.flightNumber");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> tLogs) {
		boolean twoCompleted;

		twoCompleted = tLogs.stream().filter(t -> !t.getStatus().equals(TrackingLogStatus.PENDING)).count() < 2L;

		super.getResponse().addGlobal("twoCompleted", twoCompleted);
	}
}
