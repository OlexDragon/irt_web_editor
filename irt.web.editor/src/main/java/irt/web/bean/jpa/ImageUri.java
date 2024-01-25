package irt.web.bean.jpa;

import java.net.URI;

import irt.web.controller.ImagesController.ImageStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ImageUri {

	private final URI uri;
	private ImageStatus status;

	public ImageUri(URI uri) {

		this.uri = uri;

		final String[] split = uri.toString().split("/");

		if(split.length>4) {
			status = ImageStatus.convert(split[3]);
		}else
			status = ImageStatus.UNKNOWN;
	}
}
