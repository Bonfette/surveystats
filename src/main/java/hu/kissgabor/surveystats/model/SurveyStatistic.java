package hu.kissgabor.surveystats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyStatistic {

	private Long surveyId;
	private String surveyName;
	private Long completedQuestionnaires;
	private Long filteredParticipants;
	private Long rejectedParticipants;
	private Double averageTime;
	
}
