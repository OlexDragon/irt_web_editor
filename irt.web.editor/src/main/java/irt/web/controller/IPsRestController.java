package irt.web.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import irt.web.bean.TrustStatus;
import irt.web.bean.jpa.IpConnection;
import irt.web.service.IpService;

@RestController
@RequestMapping("rest/ips")
public class IPsRestController {
	private final Logger logger = LogManager.getLogger();

	@Autowired private IpService ipService;

	@PostMapping("delete")
    String savePageValues(@RequestParam String ip) throws JsonProcessingException {

		return ipService.getIpAddress(ip)
		.map(
				ra->{
					try {

						ipService.delete(ra);
						return "IP address " + ip + " has been deleted.";

					} catch (Exception e) {

						logger.catching(e);
						return e.getLocalizedMessage();
					}
				})

		.orElse("Database does not have this ip address - " + ip);
	}

	@PostMapping("status")
    String setStatus(@RequestParam String ip, @RequestParam TrustStatus status) {

		ipService.getIpAddress(ip)
		.map(
				ra->{
					ra.setTrustStatus(status);
					try {

						ipService.save(ra);
						return "IP address status has been updated.";

					} catch (Exception e) {

						logger.catching(e);
						return e.getLocalizedMessage();
					}
				});
		return ip;
	}

	@PostMapping("connections")
    List<IpConnection> connections(@RequestParam Long ipId) {
		return ipService.getConnections(ipId);
	}
}
