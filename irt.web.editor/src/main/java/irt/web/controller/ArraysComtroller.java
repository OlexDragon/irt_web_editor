package irt.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.web.bean.jpa.Arrays;
import irt.web.bean.jpa.ArraysRepository;

@Controller
@RequestMapping("arrays")
public class ArraysComtroller {
	private final Logger logger = LogManager.getLogger();

	@Autowired private ArraysRepository arraysRepository;

	@GetMapping
    String filterGroupPage(Model model){

		final Iterable<Arrays> itArray = arraysRepository.findAll();
		logger.trace(itArray);
		model.addAttribute("arrays", itArray);

		return "arrays";
    }
}
