package irt.web.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.web.bean.jpa.ArraysRepository;
import irt.web.bean.jpa.IrtArrays;

@Controller
@RequestMapping("arrays")
public class ArraysComtroller {
	private final Logger logger = LogManager.getLogger();

	@Autowired private ArraysRepository arraysRepository;

	@GetMapping
    String filterGroupPage(@RequestParam(required = false) String name, String type, Model model){
		logger.traceEntry("name: {}; type: {}", name, type);

		final Iterable<IrtArrays> itArray = Optional.ofNullable(name)

				.map(
						n->
						Optional.ofNullable(type)
						.map(t->arraysRepository.findByArrayIdNameAndArrayIdType(n, t))
						.orElseGet(()->arraysRepository.findByArrayIdName(n)))
				.orElseGet(()->arraysRepository.findAll());

		logger.trace(itArray);
		model.addAttribute("arrays", itArray);

		return "arrays";
    }
}
