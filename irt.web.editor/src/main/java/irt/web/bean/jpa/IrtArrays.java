package irt.web.bean.jpa;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import irt.web.service.JSonListConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "arrays")
@NoArgsConstructor @RequiredArgsConstructor @Getter @Setter @EqualsAndHashCode(exclude = "content") @ToString
public class IrtArrays{

	@NonNull
	@EmbeddedId
	private ArraysId arrayId;

	@NonNull
	@Convert(converter = JSonListConverter.class)
	private List<String> content;
}
