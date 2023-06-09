package flow.cp.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="tb_text_timeline")
public class TextResultEntity {
	@Id
	@GeneratedValue(generator ="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "log_id")
	private LogEntity log;
	
	private String timeline1;
	
	private String timeline2;
	
	private float text_copy_rate;
}
