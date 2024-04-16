package irt.web.controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.web.bean.jpa.Faq;
import irt.web.bean.jpa.FaqAnswer;
import irt.web.bean.jpa.FaqAnswerRepository;
import irt.web.bean.jpa.FaqRepository;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContent.ValueType;
import irt.web.bean.jpa.WebContentRepository;
import static irt.web.controller.SupportComtroller.*; 

@RestController
@RequestMapping("rest/suport")
public class SupportRestComtroller {
	private final Logger logger = LogManager.getLogger();

	@Value("${irt.web.root.path}")
	private String rootPath;
	@Value("${irt.web.source.path}")
	private String sourcePath;

	@Autowired private WebContentRepository	 webContentRepository;
	@Autowired private FaqRepository	 	 faqRepository;
	@Autowired private FaqAnswerRepository	 answerRepository;

	@PostMapping("save-faq")
    String saveFAQ(@RequestParam(required = false) Long faqID, @RequestParam String question, @RequestParam String answer){
		logger.traceEntry("faqID: {}; question: {}; answer: {}", faqID, question, answer);

		final Faq faq = Optional.ofNullable(faqID)

				.flatMap(faqRepository::findById)
				.map(f->f.setQuestion(question))
				.map(faqRepository::save)
				.orElseGet(()->faqRepository.save(new Faq(question)));

		logger.debug(faq);

		final FaqAnswer faqAnswer = Optional.ofNullable(faqID)

				.flatMap(answerRepository::findByFaqId)
				.map(f->f.setAnswer(answer))
				.map(answerRepository::save)
				.orElseGet(()->answerRepository.save(new FaqAnswer(faq.getId(), answer)));

		logger.debug(faqAnswer);

		return "Done";
	}

	@PostMapping("gui_text")
    String uploadGui(@RequestParam String text) throws IOException{
		logger.traceEntry(text);

		final Optional<WebContent> byId = webContentRepository.findById(WEB_CONTENT_ID);

		final WebContent webContent;

		if(byId.isPresent()) {

			webContent = byId.get();
			webContent.setValue(text);

		}else 
			webContent = new WebContent(WEB_CONTENT_ID.getPageName(), WEB_CONTENT_ID.getNodeId(),  ValueType.TEXT, text);

		webContentRepository.save(webContent);

		return "Done";
    }

	@PostMapping("answer")
    String answer(@RequestParam Long faqID){
		return answerRepository.findByFaqId(faqID).map(FaqAnswer::getAnswer).orElse("");
	}
}
