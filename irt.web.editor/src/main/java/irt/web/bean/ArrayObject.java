package irt.web.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ArrayObject {

	private String name;
	private String newName;
	private String type;
	private String newType;
	private String subtype;
	private String newSubtype;
	private List<String> content;
}
