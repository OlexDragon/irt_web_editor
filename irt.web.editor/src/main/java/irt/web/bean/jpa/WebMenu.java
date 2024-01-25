package irt.web.bean.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter @Setter @ToString
public class WebMenu {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long	 id;
	@Column(nullable = true)
	private Long	 ownerId;
	private String name;
	@Column(insertable = false)
	private String link;
	@Column(insertable = false)
	private Integer menuOrder;
	@Column(insertable = false)
	private Boolean active;

	@OneToMany(targetEntity = WebMenuFilter.class, mappedBy = "filter", fetch = FetchType.LAZY)
	List<Filter> filters;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "menuId")
	List<WebMenuFilter> menuFilters;

	public List<Long> getFilterIds(){
		return Optional.ofNullable(menuFilters).map(List::stream).map(s->s.map(mf->mf.getFilterId()).collect(Collectors.toList())).orElse(new ArrayList<>());
	}
}
