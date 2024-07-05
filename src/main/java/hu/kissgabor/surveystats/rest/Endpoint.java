package hu.kissgabor.surveystats.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hu.kissgabor.surveystats.model.Member;
import hu.kissgabor.surveystats.model.MemberSurveyPoints;
import hu.kissgabor.surveystats.model.Survey;
import hu.kissgabor.surveystats.model.SurveyStatistic;
import hu.kissgabor.surveystats.service.CSVMappingService;
import hu.kissgabor.surveystats.service.SurveyAnalysisService;

@RestController
public class Endpoint {

	private CSVMappingService csvMappingService;
	private SurveyAnalysisService surveyAnalysisService;
	
	private Endpoint(CSVMappingService csvMappingService, SurveyAnalysisService surveyAnalysisService) {
		this.csvMappingService = csvMappingService;
		this.surveyAnalysisService = surveyAnalysisService;
	}
	
	// A
	@GetMapping(path = "/members/completedsurvey/{surveyId}", produces = "application/json")
	public ResponseEntity<List<Member>> membersCompletedSurvey(@PathVariable(name = "surveyId", required = true) Long surveyId) throws IOException {
		return surveyAnalysisService.membersCompletedSurvey(surveyId);
	}
	
	// B
	@GetMapping(path = "/surveys/completedbymember/{memberId}", produces = "application/json")
	public ResponseEntity<List<Survey>> surveysCompletedByMember(@PathVariable(name = "memberId", required = true) Long memberId) throws IOException {
		return surveyAnalysisService.surveysCompletedByMember(memberId);
	}
	
	// C
	@GetMapping(path = "/member/{memberId}/points", produces = "application/json")
	public ResponseEntity<List<MemberSurveyPoints>> memberPoints(@PathVariable(name = "memberId", required = true) Long memberId) throws IOException {
		return surveyAnalysisService.memberPoints(memberId);
	}
	
	// D
	@GetMapping(path = "/members/notparticipated/{surveyId}", produces = "application/json")
	public ResponseEntity<List<Member>> membersNotParticipated(@PathVariable(name = "surveyId", required = true) Long surveyId) throws IOException {
		return surveyAnalysisService.membersNotParticipated(surveyId);
	}
	
	// E
	@GetMapping(path = "/survey/statistics", produces = "application/json")
	public ResponseEntity<List<SurveyStatistic>> surveyStatistics() throws IOException {
		return surveyAnalysisService.surveyStatistics();
	}
	
	@DeleteMapping(path = "/cache/clearall")
	public void clearAllCaches() {
		csvMappingService.clearAllCaches();
	}
}
