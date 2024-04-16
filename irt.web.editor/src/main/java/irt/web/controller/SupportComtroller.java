package irt.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.web.bean.jpa.Faq;
import irt.web.bean.jpa.FaqRepository;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContent.ValueType;
import irt.web.bean.jpa.WebContentId;
import irt.web.bean.jpa.WebContentRepository;

@Controller
@RequestMapping("support")
public class SupportComtroller {
	private final Logger logger = LogManager.getLogger();

	public static final WebContentId WEB_CONTENT_ID = new WebContentId("suport", "guiText",  ValueType.TEXT);

	@Autowired private WebContentRepository	 webContentRepository;
	@Autowired private FaqRepository	 	 faqRepository;

	@GetMapping
    String support(Model model){

		webContentRepository.findById(WEB_CONTENT_ID).map(WebContent::getValue)
		.ifPresent(text->model.addAttribute(WEB_CONTENT_ID.getNodeId(), text));

		final Iterable<Faq> all = faqRepository.findAll();
		logger.trace(all);
		model.addAttribute("allFAQs", all);

		return "support";
    }
}
