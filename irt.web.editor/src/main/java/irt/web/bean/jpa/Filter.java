package irt.web.bean.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString(exclude = "mainFilter")
public class Filter{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long	id;
	private Long 	ownerId;
	private String 	name;
	private String 	description;
	@Column(insertable = false)
	private int 	filterOrder;
	// HTML if true type="radio" or type="check"
	@Column(insertable = false)
	private boolean radio;
	@Column(insertable = false)
	private boolean active;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional=true)
    @JoinColumn(name = "ownerId", referencedColumnName ="id", insertable = false, updatable = false)
    private Filter mainFilter;

    @OneToMany(mappedBy="mainFilter", fetch = FetchType.EAGER)
	@OrderBy("filterOrder")
	private List<Filter> subFilters;
}
