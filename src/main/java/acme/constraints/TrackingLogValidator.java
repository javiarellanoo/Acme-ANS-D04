
package acme.constraints;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		Boolean result;
		assert context != null;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else if (trackingLog.getStatus() != null && trackingLog.getClaim() != null && trackingLog.getResolutionPercentage() != null && trackingLog.getCreationMoment() != null && trackingLog.getLastUpdateMoment() != null
			&& trackingLog.getDraftMode() != null) {

			// Status check
			boolean correctStatus;

			if (trackingLog.getResolutionPercentage() == 100.00)
				correctStatus = !trackingLog.getStatus().equals(TrackingLogStatus.PENDING);
			else
				correctStatus = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

			super.state(context, correctStatus, "status", "acme.validation.trackingLog.status.message");

			// Resolution check
			if (trackingLog.getResolutionPercentage() == 100.00) {
				boolean hasResolution = !StringHelper.isBlank(trackingLog.getResolution());
				super.state(context, hasResolution, "resolution", "acme.validation.trackingLog.resolution.message");
			}

			// Published check
			if (!trackingLog.getDraftMode()) {
				boolean claimPublished = !trackingLog.getClaim().getDraftMode();
				super.state(context, claimPublished, "publish", "acme.validation.trackingLog.draftMode.message");
			}

			// Time stamps check
			boolean notBeforeClaim = MomentHelper.isAfterOrEqual(trackingLog.getCreationMoment(), trackingLog.getClaim().getRegistrationMoment());
			super.state(context, notBeforeClaim, "creationMoment", "acme.validation.trackingLog.momentClaim");

			boolean notUpdatedBeforeCreation = MomentHelper.isAfterOrEqual(trackingLog.getLastUpdateMoment(), trackingLog.getCreationMoment());
			super.state(context, notUpdatedBeforeCreation, "lastUpdateMoment", "acme.validation.trackingLog.updateMoment");

			// Incrementing resolution percentage check
			boolean greaterPercentage = true;

			List<TrackingLog> tLogs = this.repository.findAllByClaimId(trackingLog.getClaim().getId());
			int index = tLogs.indexOf(trackingLog);

			if (index != -1)
				tLogs.set(index, trackingLog);
			else
				tLogs.add(trackingLog);

			super.state(context, tLogs.stream().filter(t -> t.getResolutionPercentage() == 100.00).count() <= 2, "resolutionPercentage", "acme.validation.trackingLog.numberCompleted");

			tLogs.sort(Comparator.comparing(t -> t.getCreationMoment()));
			index = tLogs.indexOf(trackingLog);

			if (index > 0) {
				TrackingLog prev = tLogs.get(index - 1);
				if (prev.getResolutionPercentage() == 100.00 && trackingLog.getResolutionPercentage() == 100.00) {
					super.state(context, !prev.getDraftMode(), "status", "acme.validation.trackingLog.draftModeDissatisfaction.message");
					super.state(context, trackingLog.getStatus().equals(TrackingLogStatus.DISSATISFACTION), "status", "acme.validation.trackingLog.newCompletedTLog");
				} else
					greaterPercentage = prev.getResolutionPercentage() < trackingLog.getResolutionPercentage();
			}

			if (index < tLogs.size() - 1) {
				TrackingLog next = tLogs.get(index + 1);
				if (next.getResolutionPercentage() == 100.00 && trackingLog.getResolutionPercentage() == 100.00) {
					super.state(context, !trackingLog.getDraftMode(), "status", "acme.validation.trackingLog.draftModeDissatisfaction.message");
					super.state(context, next.getStatus().equals(TrackingLogStatus.DISSATISFACTION), "status", "acme.validation.trackingLog.newCompletedTLog");
				} else
					greaterPercentage = next.getResolutionPercentage() > trackingLog.getResolutionPercentage();
			}
			super.state(context, greaterPercentage, "resolutionPercentage", "acme.validation.trackingLog.percentage.message");

			// Unique dissatisfaction check
			if (trackingLog.getStatus().equals(TrackingLogStatus.DISSATISFACTION)) {
				boolean isUnique = tLogs.stream().filter(t -> t.getId() != trackingLog.getId()) //
					.noneMatch(t -> t.getStatus().equals(TrackingLogStatus.DISSATISFACTION));
				boolean anotherCompleted = tLogs.stream().filter(t -> t.getId() != trackingLog.getId()).anyMatch(t -> t.getResolutionPercentage() == 100.00);
				super.state(context, isUnique, "status", "acme.validation.trackingLog.statusDissatisfaction");
				super.state(context, anotherCompleted, "status", "acme.validation.trackingLog.newCompletedTLog");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
