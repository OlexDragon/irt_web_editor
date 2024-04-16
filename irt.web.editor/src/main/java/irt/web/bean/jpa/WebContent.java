package irt.web.bean.jpa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@IdClass(WebContentId.class)
@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @ToString
public class WebContent{

	@Id @NonNull private String	pageName;
	@Id @NonNull private String	nodeId;
	@Id @NonNull @Enumerated(EnumType.ORDINAL) private ValueType valueType;

	@NonNull private String 	value;

	private VariableContent variableContent;

	public WebContentId getId() {
		return new WebContentId(pageName, nodeId, valueType);
	}

	public enum ValueType{
		VALUE,
		TEXT,
		CLASS,
		HREF;
	}
}
