package irt.web.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.web.bean.ArrayObject;
import irt.web.bean.jpa.IrtArrays;
import irt.web.bean.jpa.ArraysId;
import irt.web.bean.jpa.ArraysRepository;

@RestController
@RequestMapping("arrays")
public class ArraysReastComtroller {
	private final Logger logger = LogManager.getLogger();

	@Autowired private ArraysRepository arraysRepository;

	@PostMapping("save")
	String save(@RequestBody ArrayObject arrayObject) {
		logger.traceEntry("{};", arrayObject);

		ArraysId id = new ArraysId(arrayObject.getName(), arrayObject.getType(), arrayObject.getSubtype());
		final Optional<IrtArrays> byId = arraysRepository.findById(id );
		IrtArrays arrays;
		if(byId.isPresent()) {

			ArraysId aId = new ArraysId();
			Optional.ofNullable(arrayObject.getNewName()).ifPresent(aId::setName);
			Optional.ofNullable(arrayObject.getNewType()).ifPresent(aId::setType);
			Optional.ofNullable(arrayObject.getNewSubtype()).ifPresent(aId::setSubtype);

			if(aId.fillEmpty(id).equals(id))
				arrays = byId.get();

			else {
				arrays = new IrtArrays();
				arrays.setArrayId(aId);
				arraysRepository.deleteById(id);
				logger.debug("Deleted: {}", aId);
			}

			arrays.setContent(arrayObject.getContent());

		}else
			arrays = new IrtArrays(id, arrayObject.getContent());

		arraysRepository.save(arrays);

		return "Array has been saved.";
	}

	@PostMapping("delete")
	String delete(@RequestParam String name, String type, String subtype) {
		logger.traceEntry("name: {}; type: {}; subtype: {}", name, type, subtype);

		ArraysId id = new ArraysId(name, type, subtype);

		if(arraysRepository.existsById(id))
			arraysRepository.deleteById(id);

		else
			return "No array with these settings was found.\nname: " + name + "; type: " + type + "; subtype: " + subtype;

		return "Array has been deleted.";
	}
}
