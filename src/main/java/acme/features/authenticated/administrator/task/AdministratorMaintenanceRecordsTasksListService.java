
package acme.features.authenticated.administrator.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;

@GuiService
public class AdministratorMaintenanceRecordsTasksListService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorMaintenanceRecordsTasksRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		status = maintenanceRecord != null && !maintenanceRecord.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int maintenanceRecordId;
		boolean draftMode;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);

		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		draftMode = maintenanceRecord != null && maintenanceRecord.getDraftMode();

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
		super.getResponse().addGlobal("draftMode", draftMode);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedHoursDuration");
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.addPayload(dataset, task, "type", "description", "priority", "estimatedHoursDuration", "technician.identity.fullName");
		super.getResponse().addData(dataset);
	}
}
