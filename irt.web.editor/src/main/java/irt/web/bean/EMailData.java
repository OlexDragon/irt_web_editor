package irt.web.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import irt.web.bean.content.PageValues;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContent.ValueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @Getter @Setter @ToString
public class EMailData {

	private String to;
	private String from;
	private String password;
	private String clientId;
	private String objectId;
	private String tenantId;
	private String secretId;
	private String clientSecret;	

	public EMailData(PageValues pageValues) {

		final String pageName = pageValues.getPageName();

		if(!pageName.equals("email"))
			throw new IllegalArgumentException();

		pageValues.getValues().forEach(
				c->{
					switch (c.getNodeId()) {

					case "emailTo":
						to = c.getValue();
						break;

					case "emailFrom":
						from = c.getValue();
						break;

					case "emailPassword":
						password = c.getValue();
						break;

					case "clientId":
						clientId = c.getValue();
						break;

					case "objectId":
						objectId = c.getValue();
						break;

					case "tenantId":
						tenantId = c.getValue();
						break;

					case "secretId":
						secretId = c.getValue();
						break;

					case "clientSecret":
						clientSecret = c.getValue();
						break;
					}
				});
	}

	public EMailData(List<WebContent> webContents) {

		webContents.forEach(
				c->{

					if(!c.getPageName().equals("email"))
						throw new IllegalArgumentException();

					switch (c.getNodeId()) {

					case "emailTo":
						to = c.getValue();
						break;

					case "emailFrom":
						from = c.getValue();
						break;

					case "emailPassword":
						password = Encoder.decode(c.getValue());
						break;

					case "clientId":
						clientId = c.getValue();
						break;

					case "objectId":
						objectId = c.getValue();
						break;

					case "tenantId":
						tenantId = c.getValue();
						break;

					case "secretId":
						secretId = c.getValue();
						break;

					case "clientSecret":
						clientSecret = c.getValue();
						break;
					}
				});
	}

	public List<WebContent> toWebContents(){

		List<WebContent> webContents = new ArrayList<>();

		Optional.ofNullable(to)								.map(v->new WebContent("email", "emailTo", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(from)							.map(v->new WebContent("email", "emailFrom", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(password)	.map(Encoder::encode).map(v->new WebContent("email", "emailPassword",ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(clientId)						.map(v->new WebContent("email", "clientId", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(objectId)						.map(v->new WebContent("email", "objectId", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(tenantId)						.map(v->new WebContent("email", "tenantId", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(secretId)						.map(v->new WebContent("email", "secretId", 	ValueType.TEXT, v))	.ifPresent(webContents::add);
		Optional.ofNullable(clientSecret)					.map(v->new WebContent("email", "clientSecret",ValueType.TEXT, v))	.ifPresent(webContents::add);

		return webContents;
	}
}
