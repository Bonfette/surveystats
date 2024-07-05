package hu.kissgabor.surveystats.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CSVParser {
	
	public List<List<String>> parseCsv(File csv, String delimiter) throws IOException {
		try(BufferedReader br = Files.newBufferedReader(Paths.get(csv.getAbsolutePath()))) {
			List<List<String>> csvContent = br.lines().map(line -> Arrays.asList(line.split(delimiter, -1))).toList();
			
			return csvContent;
		}
	}
	
}