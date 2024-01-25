package irt.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import irt.web.bean.jpa.RemoteAddress.TrustStatus;
import irt.web.bean.jpa.RemoteAddressRepository;

@RestController
@RequestMapping("/rest/ips")
public class IPsRestController {
	private final Logger logger = LogManager.getLogger();

	@Autowired private RemoteAddressRepository	remoteAddressRepository;

	@PostMapping("delete")
    String savePageValues(@RequestParam String ip) throws JsonProcessingException {

		return remoteAddressRepository.findById(ip)
		.map(
				ra->{
					try {

						remoteAddressRepository.delete(ra);
						return "IP address " + ip + " has been deleted.";

					} catch (Exception e) {

						logger.catching(e);
						return e.getLocalizedMessage();
					}
				})

		.orElse("Database does not have this ip address - " + ip);
	}

	@PostMapping("status")
    String status(@RequestParam String ip, @RequestParam TrustStatus status) {

		remoteAddressRepository.findById(ip)
		.map(
				ra->{
					ra.setTrustStatus(status);
					try {

						remoteAddressRepository.save(ra);
						return "IP address status has been updated.";

					} catch (Exception e) {

						logger.catching(e);
						return e.getLocalizedMessage();
					}
				});
		return ip;
	}
}
