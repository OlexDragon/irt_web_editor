package irt.web.bean.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode @ToString
public class ArraysId implements Serializable {
	private static final long serialVersionUID = -7195050765665109218L;

	private String name;
	private String type;
	private String subtype;

	public ArraysId fillEmpty(ArraysId id) {

		if(name==null)
			name = id.name;

		if(type==null)
			type = id.type;

		if(subtype==null)
			subtype = id.subtype;

		return this;
	}
}
