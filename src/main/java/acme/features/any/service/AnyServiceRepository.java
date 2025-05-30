
package acme.features.any.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.services.Service;

@Repository
public interface AnyServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.id = :id")
	Service findServiceById(int id);

	@Query("select s from Service s")
	List<Service> findAllServices(PageRequest pageRequest);

	@Query("select s from Service s")
	Collection<Service> findAllServices();

	@Query("select count(s) from Service s")
	int countServices();

	default Service findRandomService() {
		Service result;
		int count, index;
		PageRequest page;
		List<Service> list;

		count = this.countServices();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);

			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			list = this.findAllServices(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}

}
