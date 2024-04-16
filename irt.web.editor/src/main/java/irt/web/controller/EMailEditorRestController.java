package irt.web.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import irt.web.bean.EMailData;
import irt.web.bean.content.PageValues;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContentRepository;

@RestController
@RequestMapping("/rest/email")
public class EMailEditorRestController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private WebContentRepository	 	webContentRepository;

	@PostMapping("values/save")
    String savePageValues(@RequestBody PageValues pageValues) throws JsonProcessingException {
		logger.traceEntry("pageValues: {}", pageValues);

		final String pageName = pageValues.getPageName();
		if(!pageName.equals("email")) {
			logger.warn("Incorect page name ({})", pageName);
			return "Incorect page name";
		}

		final EMailData eMailData = new EMailData(pageValues);
		final List<WebContent> webContents = eMailData.toWebContents();
		logger.error("{}", webContents);

		if(!webContents.isEmpty())
			webContentRepository.saveAll(webContents);

		return "The E-Mail data has been saved.";
	}
}
