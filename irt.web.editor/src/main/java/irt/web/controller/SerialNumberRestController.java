package irt.web.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.web.bean.jpa.PartNumber;
import irt.web.bean.jpa.PartNumberRepository;
import irt.web.bean.jpa.SerialNumber;
import irt.web.bean.jpa.SerialNumberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor; 

@RestController
@RequestMapping("rest/sn")
public class SerialNumberRestController {
	private final Logger logger = LogManager.getLogger();

	@Autowired private SerialNumberRepository serialNumberRepository;
	@Autowired private PartNumberRepository partNumberRepository;

	@GetMapping("pn")
    Iterable<PartNumber> answer(@RequestParam(required = false) String search){
		logger.traceEntry("search: {}", search);
		return Optional.ofNullable(search).filter(s->!s.isEmpty()).map(s->partNumberRepository.findByPartNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(s, s)).orElseGet(()->partNumberRepository.findAll());
	}

	@PostMapping("create")
	Message create(@RequestParam String sn, Long pnId, String newPn, String description) {
		logger.traceEntry("sn: {}; pnId: {}; newPn: {}; description: {};", sn, pnId, newPn, description);

		if(serialNumberRepository.existsBySerialNumberIgnoreCase(sn))
			return new Message("Serial Number " + sn + " already exists.");

		if(!partNumberRepository.existsById(pnId))
			return new Message("Part Number with ID " + pnId + " does mot exist.");

		final SerialNumber serialNumber = new SerialNumber();
		serialNumber.setSerialNumber(sn.toUpperCase());

		if(pnId!=null) 
			serialNumber.setPartNumberId(pnId);

		else {
			if(newPn == null || newPn.isEmpty() || description==null | description.isEmpty())
				return new Message("The Part Number and Description fields must be filled in.");

			final PartNumber saved = partNumberRepository.save(new PartNumber(newPn.toUpperCase(), description));
			serialNumber.setPartNumberId(saved.getId());
		}

		logger.debug(serialNumber);
		serialNumberRepository.save(serialNumber);

		return new Message("The Serial Number " + serialNumber.getSerialNumber() + " has been saved.");
	}

	@RequiredArgsConstructor @Getter
	class Message{
		private final String Message;
	}
}
