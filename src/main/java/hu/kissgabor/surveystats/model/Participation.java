package hu.kissgabor.surveystats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participation {

	private Long memberId;
	private Long surveyId;
	private Long status;
	private Long length;
	
}
