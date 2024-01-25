package irt.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import irt.web.controller.ImagesController.ImageStatus;

@Component
public class ImageStatusConverter implements Converter<String, ImageStatus> {

	@Override
	public ImageStatus convert(String source) {
		return ImageStatus.convert(source);
	}

}
