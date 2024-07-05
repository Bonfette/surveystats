package hu.kissgabor.surveystats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hu.kissgabor.surveystats.model.Member;
import hu.kissgabor.surveystats.model.MemberSurveyPoints;
import hu.kissgabor.surveystats.model.Participation;
import hu.kissgabor.surveystats.model.Survey;
import hu.kissgabor.surveystats.model.SurveyStatistic;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SurveyAnalysisService {
	
	private final static Long REJECTED = 2L;
	private final static Long FILTERED = 3L;
	private final static Long COMPLETED = 4L;
	
	private CSVMappingService csvMappingService;
	
	public SurveyAnalysisService(CSVMappingService csvMappingService) {
		this.csvMappingService = csvMappingService;
	}
	
	public ResponseEntity<List<Member>> membersCompletedSurvey(Long surveyId) throws IOException {
		List<Member> allMembers = csvMappingService.mapCsvToMembers(",");
		List<Participation> allPart = csvMappingService.mapCsvToParticipation(",");
		
		List<Long> completedMemberIds = allPart.stream().filter(e -> surveyId.equals(e.getSurveyId()) && e.getStatus() == COMPLETED).map(e -> e.getMemberId()).toList();
		
		return ResponseEntity.ok(allMembers.stream().filter(e -> completedMemberIds.contains(e.getMemberId())).toList());
	}
	
	public  ResponseEntity<List<Survey>> surveysCompletedByMember(Long memberId) throws IOException {
		List<Survey> allSurveys = csvMappingService.mapCsvToSurveys(",");
		List<Participation> allPart = csvMappingService.mapCsvToParticipation(",");
		
		List<Long> completedSurveyIds = allPart.stream().filter(e -> memberId.equals(e.getMemberId()) && e.getStatus() == COMPLETED).map(e -> e.getSurveyId()).toList();
		
		return  ResponseEntity.ok(allSurveys.stream().filter(e -> completedSurveyIds.contains(e.getSurveyId())).toList());
	}
	
	public  ResponseEntity<List<MemberSurveyPoints>> memberPoints(Long memberId) throws IOException {
		List<Survey> allSurveys = csvMappingService.mapCsvToSurveys(",");
		
		Map<Long, Survey> surveyPoints = allSurveys.stream().collect(Collectors.toMap(Survey::getSurveyId, Function.identity()));
		
		List<Participation> allPart = csvMappingService.mapCsvToParticipation(",");
		
		List<MemberSurveyPoints> result = new ArrayList<>();
		
		allPart.stream().filter(e -> memberId.equals(e.getMemberId()) && (e.getStatus().equals(FILTERED) || e.getStatus().equals(COMPLETED)))
		.forEach(e -> result.add(new MemberSurveyPoints(e.getSurveyId(), 
				e.getStatus().equals(COMPLETED) ? surveyPoints.get(e.getSurveyId()).getCompletionPoints() : surveyPoints.get(e.getSurveyId()).getFilteredPoints()
				)));
		
		return  ResponseEntity.ok(result);
	}
	
	public ResponseEntity<List<Member>> membersNotParticipated(Long surveyId) throws IOException {
		List<Member> activeMembers = csvMappingService.mapCsvToMembers(",").stream().filter(e -> e.getIsActive().equals(1)).toList();
		List<Long> participatedInSurvey = csvMappingService.mapCsvToParticipation(",").stream()
				.filter(e -> surveyId.equals(e.getSurveyId()) && (e.getStatus().equals(FILTERED) || e.getStatus().equals(COMPLETED)))
				.map(e -> e.getMemberId())
				.toList();
				
		return  ResponseEntity.ok(activeMembers.stream().filter(e -> !participatedInSurvey.contains(e.getMemberId())).toList());
	}
	
	public  ResponseEntity<List<SurveyStatistic>> surveyStatistics() throws IOException {
		Map<Long, String> surveyNameMap = csvMappingService.mapCsvToSurveys(",").stream().collect(Collectors.toMap(Survey::getSurveyId, Survey::getName));
		
		List<SurveyStatistic> result = new ArrayList();
		
		csvMappingService.mapCsvToParticipation(",").stream()
		.collect(Collectors.groupingBy(Participation::getSurveyId, Collectors.collectingAndThen(Collectors.toList(), list -> {
		    long completed = list.stream()
		    		.filter(e -> e.getStatus().equals(COMPLETED))
		    		.collect(Collectors.counting());
		    long filtered = list.stream()
		    		.filter(e -> e.getStatus().equals(FILTERED))
		    		.collect(Collectors.counting());
		    long rejected = list.stream()
		    		.filter(e -> e.getStatus().equals(REJECTED))
		    		.collect(Collectors.counting());
		    Double averageTime = list.stream().filter(e -> e.getStatus().equals(COMPLETED))
		      .collect(Collectors.averagingDouble(Participation::getLength));
		    return new SurveyStatistic(null, null, completed, filtered, rejected, averageTime);
		}))).entrySet().stream().forEach(e -> {
			SurveyStatistic ss = e.getValue();
			ss.setSurveyId(e.getKey());
			ss.setSurveyName(surveyNameMap.get(e.getKey()));
			result.add(ss);
		});
		
		return  ResponseEntity.ok(result);
	}
}
