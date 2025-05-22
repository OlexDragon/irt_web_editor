package irt.web.bean.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ArraysRepository extends CrudRepository<IrtArrays, ArraysId> {

	Iterable<IrtArrays> findByArrayIdName(String name);
	Iterable<IrtArrays> findByArrayIdNameAndArrayIdType(String name, String type);
}
