package irt.web.bean.content;

import java.io.Serializable;
import java.util.List;

import irt.web.bean.jpa.VariableContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PageValues implements Serializable{
	private static final long serialVersionUID = -5619460017282715736L;

	private String pageName;
	private List<VariableContent> values;
}
