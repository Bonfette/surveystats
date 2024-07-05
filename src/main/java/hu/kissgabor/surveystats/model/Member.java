package hu.kissgabor.surveystats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	private Long memberId;
	private String fullName;
	private String emailAddress;
	private Integer isActive;
	
}
