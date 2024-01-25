package irt.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.web.bean.jpa.RemoteAddress;
import irt.web.bean.jpa.RemoteAddress.TrustStatus;
import irt.web.bean.jpa.RemoteAddressRepository;

@Controller
@RequestMapping("/ips")
public class IPsComtroller {
	private final Logger logger = LogManager.getLogger();

	@Autowired private RemoteAddressRepository	remoteAddressRepository;

	@GetMapping
    String filterGroupPage(Model model){

		final Iterable<RemoteAddress> remoteAddress = remoteAddressRepository.findAll();
		logger.trace(remoteAddress);
		model.addAttribute("remoteAddress", remoteAddress);
		model.addAttribute("trustStatus", TrustStatus.values());

		return "ips";
    }
}
