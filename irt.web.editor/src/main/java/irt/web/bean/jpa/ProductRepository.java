package irt.web.bean.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

	Optional<Product> 	findByPartNumber(String partNumber);
	List<Product> 		findAllByOrderByPartNumberAsc();
	List<Product> 		findByActiveOrderByPartNumberAsc(boolean b);
}
