
package acme.features.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Leg leg;

		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(masterId);
		if (leg == null)
			status = false;
		else {
			flight = this.repository.findFlightById(leg.getFlight().getId());
			status = leg.getDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && !super.getRequest().getMethod().equals("GET");
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int destinationAirportId;
		int aircraftId;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);
		Airport destinationAirport = this.repository.findAirportById(destinationAirportId);
		super.bindObject(leg, "flightNumber", "status", "scheduledDeparture", "scheduledArrival");
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setDestinationAirport(destinationAirport);
	}

}
