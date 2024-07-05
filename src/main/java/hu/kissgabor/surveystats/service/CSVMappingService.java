package hu.kissgabor.surveystats.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hu.kissgabor.surveystats.model.Member;
import hu.kissgabor.surveystats.model.Participation;
import hu.kissgabor.surveystats.model.Status;
import hu.kissgabor.surveystats.model.Survey;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CSVMappingService {

	@Value("${csv.default.delimiter}")
	private String defaultDelimiter;
	
	@Value("${csv.folder}")
	private String csvFolder;
	
	@Autowired
	private CacheManager cacheManager;

	private CSVParser csvParser;
	
	public CSVMappingService(CSVParser csvParser) {
		this.csvParser = csvParser;
	}
	
	private String getDelimiter(String delimiter) {
		if(delimiter != null)
			return delimiter;
		else
			return defaultDelimiter;
	}
	
	private List<List<String>> getParsedCsv(String filename, String delimiter) throws IOException {
		File csvFile = new File(csvFolder + filename);
		return csvParser.parseCsv(csvFile, getDelimiter(delimiter));
	}
	
	@Cacheable(cacheNames = "mapCsvToStatuses", key = "#delimiter")
	public List<Status> mapCsvToStatuses(String delimiter) throws IOException {
		log.info("mapCsvToStatuses called");
		List<List<String>> parsedCsv = getParsedCsv("Statuses.csv", getDelimiter(delimiter));
		
		return parsedCsv.stream().skip(1).map(e -> new Status(Long.parseLong(e.get(0)), e.get(1))).toList();
	}
	
	@Cacheable(cacheNames = "mapCsvToMembers", key = "#delimiter")
	public List<Member> mapCsvToMembers(String delimiter) throws IOException {
		log.info("mapCsvToMembers called");
		List<List<String>> parsedCsv = getParsedCsv("Members.csv", getDelimiter(delimiter));
		
		return parsedCsv.stream().skip(1).map(e -> new Member(Long.parseLong(e.get(0)), e.get(1), e.get(2), Integer.parseInt(e.get(3)))).toList();
	}
	
	@Cacheable(cacheNames = "mapCsvToParticipation", key = "#delimiter")
	public List<Participation> mapCsvToParticipation(String delimiter) throws IOException {
		log.info("mapCsvToParticipation called");
		List<List<String>> parsedCsv = getParsedCsv("Participation.csv", getDelimiter(delimiter));
		
		return parsedCsv.stream().skip(1).map(e -> new Participation(Long.parseLong(e.get(0)), Long.parseLong(e.get(1)), Long.parseLong(e.get(2)), e.get(3).isBlank() ? 0L : Long.parseLong(e.get(3)))).toList();
	}
	
	@Cacheable(cacheNames = "mapCsvToSurveys", key = "#delimiter")
	public List<Survey> mapCsvToSurveys(String delimiter) throws IOException {
		log.info("mapCsvToSurveys called");
		List<List<String>> parsedCsv = getParsedCsv("Surveys.csv", getDelimiter(delimiter));
		
		return parsedCsv.stream().skip(1).map(e -> new Survey(Long.parseLong(e.get(0)), e.get(1), Long.parseLong(e.get(2)), Long.parseLong(e.get(3)), Long.parseLong(e.get(4)))).toList();
	}
	
	public void clearAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
		log.info("all caches cleared");
	}
	
}
