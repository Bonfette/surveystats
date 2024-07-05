package hu.kissgabor.surveystats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Survey {

	private Long surveyId;
	private String name;
	private Long expectedCompletes;
	private Long completionPoints;
	private Long filteredPoints;
	
}
