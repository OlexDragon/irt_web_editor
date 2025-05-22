package irt.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.web.bean.jpa.PartNumber;
import irt.web.bean.jpa.SerialNumber;
import irt.web.bean.jpa.SerialNumberRepository;

@Controller
@RequestMapping("/sn")
public class SerialNumberController {
	private final Logger logger = LogManager.getLogger();

	@Autowired private EntityManager		entityManager;
	@Autowired private SerialNumberRepository serialNumberRepository;

	@GetMapping
    String serialNumber(@RequestParam(required = false) String sn, Model model){

		final Iterable<SerialNumber> sns = serialNumberRepository.findTop100ByOrderByIdDesc();
		logger.trace(sns);
		model.addAttribute("serialNumbers", sns);

		return "serial_number";
    }

	@PostMapping("content")
    String content(@RequestParam(required = false) String sn, String pn, String d, Model model){
		logger.traceEntry("sn: {}; pn: {}, d: {}", sn, pn, d);

		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<SerialNumber> criteriaQuery = criteriaBuilder.createQuery(SerialNumber.class);
		final Root<SerialNumber> root = criteriaQuery.from(SerialNumber.class);
		final Join<SerialNumber, PartNumber> join = root.join("partNumber");

		//  WHERE filter ID equals
		final List<Predicate> where = new ArrayList<>();
		Optional.ofNullable(sn).filter(s->!s.isEmpty()).ifPresent(s->where.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("serialNumber")), "%" + s.toUpperCase() + "%")));
		Optional.ofNullable(pn).filter(s->!s.isEmpty()).ifPresent(s->where.add(criteriaBuilder.like(criteriaBuilder.upper(join.get("partNumber")), "%" + s.toUpperCase() + "%")));
		Optional.ofNullable(d).filter(s->!s.isEmpty()).ifPresent(s->where.add(criteriaBuilder.like(criteriaBuilder.upper(join.get("description")), "%" + s.toUpperCase() + "%")));

		if(where.isEmpty()) {
			final Iterable<SerialNumber> sns = serialNumberRepository.findTop100ByOrderByIdDesc();
			logger.trace(sns);
			model.addAttribute("serialNumbers", sns);
		}else {
			criteriaQuery.where(where.toArray(new Predicate[where.size()]));
			final CriteriaQuery<SerialNumber> select = criteriaQuery.select(root);
			final List<SerialNumber> resultList = entityManager.createQuery(select).getResultList();
			logger.error("{}", resultList);
			model.addAttribute("serialNumbers", resultList);
		}

		return "serial_number :: content";
    }
}
