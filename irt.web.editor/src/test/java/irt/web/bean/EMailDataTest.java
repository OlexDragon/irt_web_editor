package irt.web.bean;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import irt.web.bean.jpa.WebContent;

public class EMailDataTest {

	@Test
	public void test() {

		String initial = "Oleksandr";
		EMailData eMailData = new EMailData();
		eMailData.setTo(initial);

		assertEquals(initial, eMailData.getTo());

		final List<WebContent> webContents = eMailData.toWebContents();

		assertTrue(webContents.size()==1);

		final WebContent webContent = webContents.get(0);

		assertEquals(webContent.getPageName(), "email");
		assertEquals(webContent.getNodeId(), "emailTo");
		assertNotEquals(initial, webContent.getValue());

		eMailData = new EMailData(webContents);

		assertEquals(initial, eMailData.getTo());
	}

}
