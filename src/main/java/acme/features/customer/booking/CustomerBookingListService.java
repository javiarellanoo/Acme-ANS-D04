
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		bookings = this.repository.findAllBookingsByCustomerId(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		String draftMode;

		dataset = super.unbindObject(booking, "locatorCode", "price");

		dataset.put("flight", booking.getFlight().getDisplayString());

		if (booking.getDraftMode())
			draftMode = "Not published";
		else
			draftMode = "Published";
		dataset.put("draftMode", draftMode);

		super.addPayload(dataset, booking, "travelClass", "purchaseMoment", "lastCardNibble");
		super.getResponse().addData(dataset);
	}
}
