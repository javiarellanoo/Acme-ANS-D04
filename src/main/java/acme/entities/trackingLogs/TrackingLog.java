
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTrackingLog;
import acme.entities.claims.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@ValidTrackingLog
@Table(indexes = {
	@Index(columnList = "draftMode")
})
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private TrackingLogStatus	status;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				resolution;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
