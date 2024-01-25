package irt.web.controller;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import irt.web.bean.content.PageValues;
import irt.web.bean.jpa.Filter;
import irt.web.bean.jpa.FilterRepository;
import irt.web.bean.jpa.Product;
import irt.web.bean.jpa.ProductFilter;
import irt.web.bean.jpa.ProductFilterId;
import irt.web.bean.jpa.ProductFilterRepository;
import irt.web.bean.jpa.ProductRepository;
import irt.web.bean.jpa.VariableContent;
import irt.web.bean.jpa.WebContent;
import irt.web.bean.jpa.WebContent.ValueType;
import irt.web.bean.jpa.WebContentId;
import irt.web.bean.jpa.WebContentRepository;
import irt.web.bean.jpa.WebMenu;
import irt.web.bean.jpa.WebMenuFilter;
import irt.web.bean.jpa.WebMenuFilterId;
import irt.web.bean.jpa.WebMenuFilterRepository;
import irt.web.bean.jpa.WebMenuRepository;

@RestController
@RequestMapping("/rest")
public class IrtWebEditorRestController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private WebMenuRepository	 	webMenuRepository;
	@Autowired private WebMenuFilterRepository	menuFilterRepository;
	@Autowired private FilterRepository	 		filterRepository;
	@Autowired private ProductRepository	 	productRepository;
	@Autowired private ProductFilterRepository	productFilterRepository;
	@Autowired private WebContentRepository	 	webContentRepository;

	@PostMapping("filter")
    Filter filter(		@RequestParam(required = false) Long 	filterId, 
    					@RequestParam(required = false) Long 	ownerId, 
    					@RequestParam 					String 	filterpName, 
    					@RequestParam(required = false) String 	description, 
    					@RequestParam 					Integer order, 
    					@RequestParam 					Boolean radio, 
    					@RequestParam 					Boolean active) {

		logger.traceEntry("filterId: {}, ownerId:{}, groupName: {}, description: {}, order: {}, radio: {}, active: {}", filterId, ownerId, filterpName, description, order, radio, active);

    	final Filter filter;
    	if(filterId==null) {
    		filter = new Filter();
     	}else {
    		final Optional<Filter> oFilter = filterRepository.findById(filterId);
    		if(!oFilter.isPresent())
    			return null;
    		filter = oFilter.get();
    	}


   		filter.setName(filterpName);
  		Optional.ofNullable(ownerId).ifPresent(filter::setOwnerId);
  		Optional.ofNullable(description).filter(d->!d.isEmpty()).ifPresent(filter::setDescription);
 		filter.setFilterOrder(order);
 		filter.setRadio(radio);
 		filter.setActive(active);
		return filterRepository.save(filter);
    }

	@PostMapping("menu_name")
    WebMenu menuName(	@RequestParam(required = false) Long 	menuId, 
    					@RequestParam(required = false) Long 	ownerId, 
    					@RequestParam 					String 	menuNeme, 
    					@RequestParam 					String 	link, 
    					@RequestParam 					Integer order, 
    					@RequestParam 					Boolean active) {

		logger.traceEntry("{} : {} : {} : {}", menuId, menuNeme, link, active);

    	final WebMenu menu;
    	if(menuId==null) {
    		menu = new WebMenu();
    		Optional.ofNullable(ownerId).ifPresent(menu::setOwnerId);
    	}else {
    		Optional<WebMenu> m = webMenuRepository.findById(menuId);
        	if(!m.isPresent())
        		return null;
        	menu = m.get();
        	menu.setName(link);
        	menu.setLink(link);
        	menu.setMenuOrder(order);
        	menu.setActive(active);
    	}


    	menu.setName(menuNeme);

    	return webMenuRepository.save(menu);
    }

	@PostMapping("page_valiables")
    List<VariableContent> getPageVariables(@RequestParam String pageName) {
		logger.traceEntry(pageName);

    	return webContentRepository.findByPageName(pageName).stream().map(WebContent::getVariableContent).collect(Collectors.toList());
    }

	@PostMapping("menu/filter")
    void menuFilter(@RequestParam Long menuId, @RequestParam(name = "checked[]") List<Long> checked) {
		logger.traceEntry("{}; {}", menuId, checked);

		webMenuRepository.findById(menuId)
		.ifPresent(
				m->{

					final List<Long> filterIds = m.getFilterIds();

					if(checked.isEmpty())
						removeMenuFilters(menuId, filterIds);

					else if(filterIds.isEmpty()) 
						addMenuFilters(menuId, checked);

					else {

						List<Long> toRemove = new ArrayList<>(filterIds);
						toRemove.removeAll(checked);

						if(!toRemove.isEmpty()) 
							removeMenuFilters(menuId, toRemove);

						checked.removeAll(filterIds);

						if(!checked.isEmpty()) 
							addProductFilters(menuId, checked);
					}
				});
	}

	@PostMapping("product")
    String saveProduct(	@RequestParam(required = false) Long productId, 
    					@RequestParam String name, 
    					@RequestParam String partNumber, 
    					@RequestParam Boolean active) {

		logger.traceEntry("productId: {}; name: {}", productId, name);

		Product product;
		if(productId==null) {

			if(productRepository.findByPartNumber(partNumber).isPresent())
				return "Pert Number '" + partNumber + "' already exists.";

			product = new Product();
		}else {

			final Optional<Product> findById = productRepository.findById(productId);
			if(!findById.isPresent())
				return "Could not find this product in the database.";

			product = findById.get();
			product.setActive(active);
		}

		product.setName(name);
		product.setPartNumber(partNumber);

		productRepository.save(product);

		return "";
	}

	@PostMapping("product/filter")
    void productFilter(@RequestParam Long productId, @RequestParam(name = "checked[]") List<Long> checked) {
		logger.traceEntry("{}; {}", productId, checked);

		productRepository.findById(productId)
		.ifPresent(
				p->{

					final List<Long> filterIds = p.getFilterIds();

					if(checked.isEmpty())
						removeProductFilters(productId, filterIds);

					else if(filterIds.isEmpty()) 
						addProductFilters(productId, checked);

					else {

						List<Long> toRemove = new ArrayList<>(filterIds);
						toRemove.removeAll(checked);

						if(!toRemove.isEmpty()) 
							removeProductFilters(productId, toRemove);

						checked.removeAll(filterIds);

						if(!checked.isEmpty()) 
							addProductFilters(productId, checked);
					}
				});
	}

	@PostMapping("values/save")
    List<SimpleEntry<String, String>> savePageValues(@RequestBody PageValues pageValues) throws JsonProcessingException {
		logger.traceEntry("pageValues: {}", pageValues);

		final String pageName = pageValues.getPageName();
		List<SimpleEntry<String, String>> messages = pageValues.getValues().stream()

				.map(
						wc->{

							final String nodeId = wc.getNodeId();
							final String value = wc.getValue();
							final ValueType valueType = wc.getValueType();

							final Optional<WebContent> oWebContent = webContentRepository.findById(new WebContentId(pageName, nodeId, valueType));
							String message;
							WebContent webContent;

							if(oWebContent.isPresent()) {

								webContent = oWebContent.get();
								final String v = webContent.getValue();

								if(v.equals(value))
									return new AbstractMap.SimpleEntry<String, String>(nodeId, valueType + "- The value has not changed.");

								webContent.setValue(value);
								webContent.setVariableContent(new VariableContent(nodeId, valueType, value));
								message = valueType + "- Updated: " + webContent;

							}else {
								webContent = new WebContent(pageName, nodeId, valueType, value, new VariableContent(nodeId, valueType, value));
								message = valueType + "- Incerted: " + webContent;
							}

							try {

								webContentRepository.save(webContent);

							}catch (Exception e) {
								logger.catching(e);
								message = e.getLocalizedMessage();
							}

							return new AbstractMap.SimpleEntry<String, String>(nodeId, message);
						}).collect(Collectors.toList());

		return messages;
	}

	public void addMenuFilters(Long menuId, List<Long> toAdd) {
		logger.traceEntry("menuId: {}; toAdd: {}", menuId, toAdd);

		final List<WebMenuFilter> menuFilter = toAdd.parallelStream()

				.map(
						fId->{
							final WebMenuFilter mf = new WebMenuFilter();
							mf.setMenuId(menuId);
							mf.setFilterId(fId);
							return mf;
						})
				.collect(Collectors.toList());

		if(!menuFilter.isEmpty())
			menuFilterRepository.saveAll(menuFilter);
	}

	public void removeMenuFilters(Long menuId, final List<Long> toRemove) {
		logger.traceEntry("menuId: {}; toRemove: {}", menuId, toRemove);

		final List<WebMenuFilterId> ids = toRemove.parallelStream()

				.map(
						fId->{
							final WebMenuFilterId id = new WebMenuFilterId();
							id.setMenuId(menuId);
							id.setFilterId(fId);
							return id;
						})
				.collect(Collectors.toList());

		if(!ids.isEmpty())
			menuFilterRepository.deleteAllById(ids);
	}

	public void addProductFilters(Long productId, List<Long> toAdd) {
		logger.traceEntry("productId: {}; toAdd: {}", productId, toAdd);

		final List<ProductFilter> productFilter = toAdd.parallelStream()

				.map(
						fId->{
							final ProductFilter product = new ProductFilter();
							product.setProductId(productId);
							product.setFilterId(fId);
							return product;
						})
				.collect(Collectors.toList());

		if(!productFilter.isEmpty())
			productFilterRepository.saveAll(productFilter);
	}

	public void removeProductFilters(Long productId, final List<Long> toRemove) {
		logger.traceEntry("productId: {}; toRemove: {}", productId, toRemove);

		final List<ProductFilterId> ids = toRemove.parallelStream()

				.map(
						fId->{
							final ProductFilterId id = new ProductFilterId();
							id.setProductId(productId);
							id.setFilterId(fId);
							return id;
						})
				.collect(Collectors.toList());

		if(!ids.isEmpty())
			productFilterRepository.deleteAllById(ids);
	}
}
