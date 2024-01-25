package irt.web.bean.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@IdClass(ProductFilterId.class)
@Getter @Setter @ToString(exclude = {"filter"})
public class ProductFilter implements Serializable{
	private static final long serialVersionUID = 6549375812892233906L;

	@Id private Long	 productId;
	@Id private Long	 filterId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filterId", referencedColumnName = "id", insertable = false, updatable = false)
    private Filter filter;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="productId", referencedColumnName = "id", insertable = false, updatable = false)
//    private Product product;
}
