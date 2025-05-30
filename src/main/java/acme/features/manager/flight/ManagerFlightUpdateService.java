
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightUpdateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean airlineStatus;
		int masterId;
		Flight flight;
		Manager manager;
		int airlineId;
		Airline airline;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getManager();
		if (flight == null)
			status = false;
		else if (!flight.getDraftMode() || !super.getRequest().getPrincipal().hasRealm(manager))
			status = false;
		else if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			airlineId = super.getRequest().getData("airline", int.class);
			airline = this.repository.findAirlineById(airlineId);
			airlineStatus = airlineId == 0 || airline != null;

			status = airlineStatus;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		flight.setAirline(airline);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void unbind(final Flight flight) {
		Collection<Airline> airlines;
		SelectChoices choices;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "name", flight.getAirline());

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

}
