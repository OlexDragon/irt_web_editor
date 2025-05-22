package irt.web.bean.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.web.bean.TrustStatus;

public interface IpAddressRepository extends CrudRepository<IpAddress, Long> {

	Optional<IpAddress> findByAddress(String ip);
	Iterable<IpAddress> findByAddressOrTrustStatus(String ip, TrustStatus status);
	Iterable<IpAddress> findTop100ByOrderByIdDesc();
}
