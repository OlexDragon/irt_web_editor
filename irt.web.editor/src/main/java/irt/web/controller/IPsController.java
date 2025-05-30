package irt.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.web.bean.TrustStatus;
import irt.web.bean.jpa.IpAddress;
import irt.web.service.IpService;

@Controller
@RequestMapping("/ips")
public class IPsController {
	private final Logger logger = LogManager.getLogger();

	@Autowired private IpService ipService;

	@GetMapping
    String filterGroupPage(@RequestParam(required = false) String ip, Model model){

		final Iterable<IpAddress> ipAddress = ipService.find(ip);
		logger.trace(ipAddress);
		model.addAttribute("ipAddress", ipAddress);
		model.addAttribute("trustStatus", TrustStatus.values());

		return "ips";
    }
}
