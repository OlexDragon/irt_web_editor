package irt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.web.bean.EMailData;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContentRepository;

@Controller
@RequestMapping("/email")
public class EMailEditorComtroller {
//	private final Logger logger = LogManager.getLogger();

	@Autowired private WebContentRepository	webContentRepository;

	@GetMapping
    String filterGroupPage(Model model){

		final List<WebContent> contents = webContentRepository.findByPageName("email");
		final EMailData eMailData = new EMailData(contents);

		model.addAttribute("eMailData", eMailData);

		return "email";
    }
}
