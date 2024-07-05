package hu.kissgabor.surveystats;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hu.kissgabor.surveystats.model.Member;
import hu.kissgabor.surveystats.model.MemberSurveyPoints;
import hu.kissgabor.surveystats.model.Survey;
import hu.kissgabor.surveystats.model.SurveyStatistic;
import hu.kissgabor.surveystats.service.SurveyAnalysisService;

@SpringBootTest
public class SurveyAnalysisServiceTests {
    
    @Autowired
    private SurveyAnalysisService surveyAnalysisService;
    
    @Test
    public void membersCompletedSurvey_givenValidParam_thenStatus200() throws Exception {
    	Long surveyId = 1L;
    	
    	ResponseEntity<List<Member>> result = surveyAnalysisService.membersCompletedSurvey(surveyId);
    	
    	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    
    @Test
    public void membersCompletedSurvey_givenSurveyId4_thenReturnListOfSize1() throws Exception {
    	Long surveyId = 4L;
    	
    	ResponseEntity<List<Member>> result = surveyAnalysisService.membersCompletedSurvey(surveyId);
    	
    	Assertions.assertEquals(1, result.getBody().size());
    }
    
    @Test
    public void membersCompletedSurvey_givenSurveyId1_thenReturnCorrectList() throws Exception {
    	Long surveyId = 1L;
    	
    	Member member1 = new Member(1L, "MemberAllSurvey", "testmember1@gmail.com", 1);
    	Member member2 = new Member(3L, "MemberInactive", "testmember3@gmail.com", 0);
    	List<Member> correctResult = Arrays.asList(member1, member2);
    	
    	ResponseEntity<List<Member>> result = surveyAnalysisService.membersCompletedSurvey(surveyId);
    	
    	Assertions.assertEquals(correctResult, result.getBody());
    }
    
    @Test
    public void surveysCompletedByMember_givenMemberId2_thenReturnEmptyList() throws Exception {
    	Long memberId = 2L;
    	
    	ResponseEntity<List<Survey>> result = surveyAnalysisService.surveysCompletedByMember(memberId);
    	
    	Assertions.assertEquals(0, result.getBody().size());
    }
    
    @Test
    public void surveysCompletedByMember_givenMemberId1_thenReturnCorrectListSize() throws Exception {
    	Long memberId = 1L;
    	
    	ResponseEntity<List<Survey>> result = surveyAnalysisService.surveysCompletedByMember(memberId);
    	
    	Assertions.assertEquals(3, result.getBody().size());
    }
    
    @Test
    public void memberPoints_givenMemberId1_thenReturnCorrectList() throws Exception {
    	Long memberId = 1L;
    	
    	MemberSurveyPoints obj1 = new MemberSurveyPoints(1L, 5L);
    	MemberSurveyPoints obj2 = new MemberSurveyPoints(3L, 10L);
    	MemberSurveyPoints obj3 = new MemberSurveyPoints(4L, 1L);
    	MemberSurveyPoints obj4 = new MemberSurveyPoints(5L, 35L);
    	
    	List<MemberSurveyPoints> correctList = Arrays.asList(obj1, obj2, obj3, obj4);
    	
    	ResponseEntity<List<MemberSurveyPoints>> result = surveyAnalysisService.memberPoints(memberId);
    	
    	Assertions.assertEquals(correctList, result.getBody());
    }
    
    @Test
    public void membersNotParticipated_givenSurveyId2_thenReturnCorrectList() throws Exception {
    	Long surveyId = 2L;
    	
    	Member member1 = new Member(1L, "MemberAllSurvey", "testmember1@gmail.com", 1);
    	Member member2 = new Member(2L, "MemberNoSurvey", "testmember2@gmail.com", 1);
    	
    	List<Member> correctList = Arrays.asList(member1, member2);
    	
    	ResponseEntity<List<Member>> result = surveyAnalysisService.membersNotParticipated(surveyId);
    	
    	Assertions.assertEquals(correctList, result.getBody());
    }
    
    @Test
    public void surveyStatisticsTest() throws Exception {
    	
    	SurveyStatistic obj1 = new SurveyStatistic(1L, "SurveyTest 01", 2L, 0L, 0L, 12.5);
    	SurveyStatistic obj2 = new SurveyStatistic(2L, "SurveyTest 02", 0L, 0L, 1L, 0.0);
    	SurveyStatistic obj3 = new SurveyStatistic(3L, "SurveyTest 03", 1L, 0L, 1L, 24.0);
    	SurveyStatistic obj4 = new SurveyStatistic(4L, "SurveyTest 04", 1L, 1L, 0L, 8.0);
    	SurveyStatistic obj5 = new SurveyStatistic(5L, "SurveyTest 05", 1L, 0L, 0L, 6.0);
    	
    	List<SurveyStatistic> correctList = Arrays.asList(obj1, obj2, obj3, obj4, obj5);
    	
    	ResponseEntity<List<SurveyStatistic>> result = surveyAnalysisService.surveyStatistics();
    	
    	Assertions.assertEquals(correctList, result.getBody());
    }
    
}
