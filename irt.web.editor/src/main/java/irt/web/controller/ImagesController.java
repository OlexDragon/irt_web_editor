package irt.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("images")
public class ImagesController {
	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.web.source.path}")
	private String applicationPath;

	@Value("${irt.web.product.images.path}")
	private String productImagesPath;

	@GetMapping("product/{productId}/{imageStatus}/{subfolder}/{fileName}")
	public ResponseEntity<Resource> image(@PathVariable  Long productId, @PathVariable ImageStatus imageStatus, @PathVariable Long subfolder, @PathVariable String fileName) throws IOException{
		logger.error("productId: {}; imageStatus: {}; subfolder: {}; fileName: {}", productId, imageStatus, subfolder, fileName);

		final Path path = Paths.get(productImagesPath, productId.toString(), imageStatus.toString(), subfolder.toString(), fileName);

		return getImage(path);
	}

	@GetMapping("product/{productId}/{imageStatus}/{fileName}")
	public ResponseEntity<Resource> image(@PathVariable  Long productId, @PathVariable ImageStatus imageStatus, @PathVariable String fileName) throws IOException{
		logger.error("productId: {}; imageStatus: {}; fileName: {}", productId, imageStatus, fileName);

		final Path path = Paths.get(productImagesPath, productId.toString(), imageStatus.toString(), fileName);

		return getImage(path);
	}

	private ResponseEntity<Resource> getImage(final Path path) throws FileNotFoundException {
		final File file = path.toFile();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

		if(!file.exists()) {
			logger.error(file);
			return ResponseEntity.notFound()
					.headers(headers)
					.build();
		}

		final InputStream is = new FileInputStream(file);

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(is));
	}

	@PostMapping(path="/product/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String addImages(@RequestParam Long productId, @RequestPart("fileToAttach") List<MultipartFile> files) {
		logger.error("productIdL {}", productId);

		String subfolderName = Long.toString(System.currentTimeMillis());
		files.stream().forEach(saveImage(productId, subfolderName));

		return"Done";
	}

	private Consumer<? super MultipartFile> saveImage(Long productId, String subfolderName) {
		return  mpFile->{

			try {
				String originalFilename = mpFile.getOriginalFilename();
				Path path = Paths.get(productImagesPath, productId.toString(), ImageStatus.ACTIVE.toString(), subfolderName);

				final boolean mkdirs = path.toFile().mkdirs();	//create a directories
				logger.error(mkdirs);

				path = Paths.get(path.toString(), originalFilename);
				logger.error(path);

				try { mpFile.transferTo(path); } catch (IllegalStateException | IOException e) { logger.catching(e); }
			} catch (Exception e) {
				logger.catching(e);
			}

		};
	}

	@RequiredArgsConstructor
	public enum ImageStatus{
		ACTIVE("active"),
		DISABLED("disabled"),
		UNKNOWN("unknown");

		private final String status;

		public String toString() {
			return status;
		}

		public static ImageStatus convert(String source) {

			for(ImageStatus is: values()) {

				logger.error("{}: {} == {} -> {}", is.name(), is, source, is.status.equals(source));
				if(is.status.equals(source))
					return is;
			}

			return UNKNOWN;
		}
	}
}
