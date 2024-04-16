package irt.web.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import irt.web.bean.ConnectTo;
import irt.web.bean.TrustStatus;
import irt.web.bean.jpa.IpAddress;
import irt.web.bean.jpa.IpAddressRepository;
import irt.web.bean.jpa.IpConnection;
import irt.web.bean.jpa.IpConnectionRepository;

@Service
public class IpService {

	@Autowired private IpAddressRepository	 	ipAddressRepository;
	@Autowired private IpConnectionRepository	ipConnectionRepository;

	public Optional<IpAddress> getIpAddress(String ip) {
		return Optional.ofNullable(ip).map(ipAddressRepository::findByAddress).map(o->o.orElseGet(createIpAddress(ip)));
	}

	public List<IpConnection> getConnections(Long ipId, ConnectTo connectTo, LocalDateTime startMonth) {
		return ipConnectionRepository.findByIpIdAndConnectToAndDateGreaterThan(ipId, connectTo, startMonth);
	}

	public void save(IpAddress ipAddress) {
		ipAddressRepository.save(ipAddress);
	}

	private Supplier<IpAddress> createIpAddress(String ip) {
		return ()->{

			final IpAddress entity = new IpAddress();
			final LocalDateTime now = LocalDateTime.now(ZoneId.of("Canada/Eastern"));

			entity.setAddress(ip);
			entity.setFirstConnection(now);
			entity.setTrustStatus(TrustStatus.UNKNOWN);

			return ipAddressRepository.save(entity);
		};
	}

	public void createConnection(Long ipId, ConnectTo connectTo) {
		ipConnectionRepository.save(new IpConnection(ipId, LocalDateTime.now(), connectTo));
	}

	public Iterable<IpAddress> findAll() {
		return ipAddressRepository.findAll();
	}

	public void delete(IpAddress ipAddress) {
		ipAddressRepository.delete(ipAddress);
	}

	public List<IpConnection> getConnections(Long ipId) {
		return ipConnectionRepository.findByIpId(ipId);
	}
}
