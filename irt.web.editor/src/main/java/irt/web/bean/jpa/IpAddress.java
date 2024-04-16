package irt.web.bean.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import irt.web.bean.TrustStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class IpAddress implements Serializable{
	private static final long serialVersionUID = 6315117908794414784L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String 	address;
	private LocalDateTime 	firstConnection;
	@Enumerated(EnumType.ORDINAL)
	private TrustStatus trustStatus;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "ipId", referencedColumnName = "id", insertable = false, updatable = false)
	private List<IpConnection> connections;
}
