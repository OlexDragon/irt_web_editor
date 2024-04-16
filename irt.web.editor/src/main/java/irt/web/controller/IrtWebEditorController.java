package irt.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.web.bean.jpa.Filter;
import irt.web.bean.jpa.FilterRepository;
import irt.web.bean.jpa.ImageUri;
import irt.web.bean.jpa.Product;
import irt.web.bean.jpa.ProductRepository;
import irt.web.bean.jpa.WebMenu;
import irt.web.bean.jpa.WebMenuRepository;

@Controller
@RequestMapping("/")
public class IrtWebEditorController {
	public static final String HOME_PAGE = "home_page";

	private final Logger logger = LogManager.getLogger();

	@Value("${irt.web.source.path}")
	private String sourcePath;
	@Value("${irt.web.product.images.path}")
	private String productImagesPath;

	@Autowired private FilterRepository	 		filterRepository;
	@Autowired private WebMenuRepository	 	webMenuRepository;
	@Autowired private ProductRepository	 	productRepository;

	@GetMapping
    String filterGroupPage(Model model){

		final Iterable<Filter> filterGroups = filterRepository.findByOwnerIdIsNullOrderByFilterOrderAsc();
		model.addAttribute("groups", filterGroups);
		return "filter_group";
    }

	@GetMapping("filters")
    String filterPage(Model model){

		final Iterable<Filter> filterGroups = filterRepository.findByOwnerIdIsNullOrderByFilterOrderAsc();
		model.addAttribute("groups", filterGroups);

		return "filter";
    }

	@GetMapping("filter_options")
    String filters(@RequestParam Long ownerId, Model model){

		final Iterable<Filter> filters = filterRepository.findByOwnerIdOrderByFilterOrderAsc(ownerId);
		model.addAttribute("filters", filters);

		return "filter :: filter_options";
    }

	@GetMapping("menu")
    String menues(Model model){

		final Iterable<WebMenu> menus = webMenuRepository.findByOwnerIdIsNullOrderByMenuOrderAsc();
		model.addAttribute("menus", menus);

		final List<Filter> filters = filterRepository.findByOwnerIdIsNullAndActiveOrderByFilterOrderAsc(true);
		model.addAttribute("filters", filters);

		return "menu";
    }

	@GetMapping("submenu")
    String submenues(Model model){

		final Iterable<WebMenu> menus = webMenuRepository.findByOwnerIdIsNullOrderByMenuOrderAsc();
		model.addAttribute("menus", menus);

		final List<Filter> filters = filterRepository.findByOwnerIdIsNullAndActiveOrderByFilterOrderAsc(true);
		model.addAttribute("filters", filters);

		return "submenu";
    }

	@GetMapping("submenu_options")
    String submenueOptions(@RequestParam Long ownerId, Model model){

		final Iterable<WebMenu> submenus = webMenuRepository.findByOwnerIdOrderByMenuOrderAsc(ownerId);
		model.addAttribute("submenus", submenus);

		return "submenu :: submenu_options";
    }

	@GetMapping("products")
    String products(Model model){

		final List<Filter> filters = filterRepository.findByOwnerIdIsNullAndActiveOrderByFilterOrderAsc(true);
		model.addAttribute("filters", filters);

		final List<Product> products = productRepository.findAllByOrderByPartNumberAsc();
		model.addAttribute("products", products);

		return "products";
    }

	@GetMapping("products/images")
    String productImages(@RequestParam Long productId, Model model) throws IOException{
		logger.error("productId: {}", productId);

		getAllImagePaths(productId, model);

		return "products :: imageContent";
    }

	@GetMapping("products_by_filter")
    String productsByFilter(Model model){

		final List<Filter> filters = filterRepository.findByOwnerIdIsNullAndActiveOrderByFilterOrderAsc(true);
		model.addAttribute("filters", filters);

		final List<Product> products = productRepository.findByActiveOrderByPartNumberAsc(true);
		model.addAttribute("products", products);

		return "products_by_filter";
    }

	@GetMapping(HOME_PAGE)
    String homePageEditor(Model model){
		return HOME_PAGE;
    }

	private void getAllImagePaths(Long productId, Model model) throws IOException {

		final Path path = Paths.get(productImagesPath, productId.toString());
		final File folder = path.toFile();

		logger.error(folder);
		if(!folder.exists()) {
			folder.mkdirs();
			return;
		}

		try (Stream<Path> stream = Files.walk(path)) {

			final List<ImageUri> paths = stream.filter(Files::isRegularFile).map(Path::toUri).map(uri->Paths.get(sourcePath).toUri().relativize(uri)).map(ImageUri::new)

					.sorted(
							(a,b)->{
								final int compareStatus = a.getStatus().compareTo(b.getStatus());
								if(compareStatus!=0)
									return compareStatus;

								return a.getUri().compareTo(b.getUri());
							})
					.collect(Collectors.toList());

			model.addAttribute("images", paths);
			logger.error(paths);
		}
	}
}
