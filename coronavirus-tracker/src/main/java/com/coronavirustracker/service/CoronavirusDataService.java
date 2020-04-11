package com.coronavirustracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.coronavirustracker.constants.InterfaceConstants;
import com.coronavirustracker.dao.IndiaCoronadataDAO;
import com.coronavirustracker.dao.LocationStatsDAO;
import com.coronavirustracker.dao.LocationStatsDeathDAO;
import com.coronavirustracker.dto.AllDataCountryWiseDTO;
import com.coronavirustracker.dto.CountryWithCasesDTO;
import com.coronavirustracker.dto.CountryWithDeathCasesDTO;
import com.coronavirustracker.dto.DataDTO;
import com.coronavirustracker.feignclient.CovidDataClient;
import com.coronavirustracker.functionalinterfaces.MathCalculation;
import com.coronavirustracker.model.IndiaCoronadata;
import com.coronavirustracker.model.LocationStats;
import com.coronavirustracker.model.LocationStatsDeath;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * @author sidharthdas
 *
 */

@Service
public class CoronavirusDataService {

	private List<LocationStats> allStats = new ArrayList<>();

	@Autowired
	private LocationStatsDAO locationStatsDAO;

	@Autowired
	private LocationStatsDeathDAO locationStatsDeathDAO;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IndiaCoronadataDAO indiaCoronadataDAO;

	@Autowired
	private CovidDataClient covidDataClient;

	MathCalculation mathCalculation = (int totalCount, int sectionWise) -> {
		return (Double.valueOf(sectionWise) / Double.valueOf(totalCount)) * 100;
	};

	@PostConstruct
	@Scheduled(cron = "* 5 * * * *")
	public void fetchVirusData() throws IOException, InterruptedException {
		List<LocationStats> newStats = new ArrayList<>();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(InterfaceConstants.VIRUS_DATA_URL)).build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

		StringReader csvBodyReader = new StringReader(httpResponse.body());

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStats locationStat = new LocationStats();
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
			locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
			locationStat.setPreviousTotalCases(Integer.parseInt(record.get(record.size() - 2)));
			if (locationStatsDAO.presentDayCases(locationStat.getCountry(), locationStat.getState()).size() == 0) {
				locationStatsDAO.save(locationStat);
			} else {
				locationStatsDAO.updateCases(locationStat.getLatestTotalCases(), locationStat.getPreviousTotalCases(),
						locationStat.getCountry(), locationStat.getState());
			}
			newStats.add(locationStat);
		}
		HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(InterfaceConstants.VIRUS_DATA_DEATH_URL))
				.build();
		HttpResponse<String> httpResponse1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

		StringReader csvBodyReader1 = new StringReader(httpResponse1.body());

		Iterable<CSVRecord> records1 = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader1);
		for (CSVRecord record : records1) {
			LocationStatsDeath locationStatsDeath = new LocationStatsDeath();
			System.out.println(record.get("Country/Region"));
			locationStatsDeath.setCountry(record.get("Country/Region"));
			locationStatsDeath.setState(record.get("Province/State"));
			locationStatsDeath.setTotalNoOfDeathPresentDay(Integer.parseInt(record.get(record.size() - 1)));
			locationStatsDeath.setTotalNoOfDeathPreviousDay(Integer.parseInt(record.get(record.size() - 2)));

			if (locationStatsDeathDAO
					.getDataOfSpecificCountryAndState(locationStatsDeath.getCountry(), locationStatsDeath.getState())
					.size() == 0) {
				locationStatsDeathDAO.save(locationStatsDeath);
			} else {
				locationStatsDeathDAO.updateData(locationStatsDeath.getCountry(), locationStatsDeath.getState(),
						locationStatsDeath.getTotalNoOfDeathPresentDay(),
						locationStatsDeath.getTotalNoOfDeathPreviousDay());
			}

		}

	}

	public String getFallbackDetailsOfAllPatientInIndia() {
		System.out.println("Fallback METHOD");
		return "In Fallback method";
	}


	@PostConstruct
	@HystrixCommand(fallbackMethod = "getFallbackDetailsOfAllPatientInIndia", commandProperties = {

			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5060") })
	public String getDetailsOfAllPatientInIndia() {
		DataDTO allData = restTemplate.getForObject(InterfaceConstants.COVID_INDIA_RAW_DATA_URL, DataDTO.class);
		allData.getRaw_data().forEach(patient -> {

			if (indiaCoronadataDAO.checkPatient(patient.getPatientnumber()).size() == 0) {
				IndiaCoronadata indiaCoronadata = new IndiaCoronadata();
				indiaCoronadata.setAgebracket(patient.getAgebracket());
				indiaCoronadata.setBackupnotes(patient.getBackupnotes());
				indiaCoronadata.setCurrentstatus(patient.getCurrentstatus());
				indiaCoronadata
						.setContractedfromwhichpatientsuspected(patient.getContractedfromwhichpatientsuspected());
				indiaCoronadata.setDateannounced(patient.getDateannounced());
				indiaCoronadata.setDetectedcity(patient.getDetectedcity());
				indiaCoronadata.setDetecteddistrict(patient.getDetecteddistrict());
				indiaCoronadata.setDetectedstate(patient.getDetectedstate());
				indiaCoronadata.setEstimatedonsetdate(patient.getEstimatedonsetdate());
				indiaCoronadata.setGender(patient.getGender());
				indiaCoronadata.setNationality(patient.getNationality());
				indiaCoronadata.setPatientnumber(patient.getPatientnumber());
				indiaCoronadata.setNotes(patient.getNotes());
				indiaCoronadata.setTypeoftransmission(patient.getTypeoftransmission());

				indiaCoronadataDAO.save(indiaCoronadata);
			} else {
				indiaCoronadataDAO.updatePatientDetails(patient.getPatientnumber(), patient.getCurrentstatus());
			}

		});
		return "In main method";
	}

	public List<LocationStats> getAllStats() {
		return allStats;
	}

	public List<CountryWithCasesDTO> getCountryWithCases() {

		List<CountryWithCasesDTO> countryWiseCases = new ArrayList<>();
		Set<String> allCountries = locationStatsDAO.allCountry().stream().collect(Collectors.toSet());
		allCountries.forEach(country -> {

			int totalPresentCases = locationStatsDAO.totalPresentCaseOfCountry(country).stream().mapToInt(x -> x).sum();
			int totalPreviousCases = locationStatsDAO.totalPreviousCaseOfCountry(country).stream().mapToInt(x -> x)
					.sum();

			CountryWithCasesDTO countryWithCaseDTO = new CountryWithCasesDTO();
			countryWithCaseDTO.setCountryName(country);
			countryWithCaseDTO.setPresentDayCases(totalPresentCases);
			countryWithCaseDTO.setPreviousDayCases(totalPreviousCases);

			countryWiseCases.add(countryWithCaseDTO);

		});

		return countryWiseCases;
	}

	public List<CountryWithDeathCasesDTO> getCountryWithDeathCases() {
		List<CountryWithDeathCasesDTO> countryWithDeathCases = new ArrayList<>();
		Set<String> allCountries = locationStatsDeathDAO.allCountriesDeath().stream().collect(Collectors.toSet());
		allCountries.forEach(country -> {

			int totalDeathToday = locationStatsDeathDAO.listOfDeathReportedCountryWiseToday(country).stream()
					.mapToInt(x -> x).sum();
			int totalDeathPreviousDay = locationStatsDeathDAO.listOfDeathReportedCountryWisePrevious(country).stream()
					.mapToInt(x -> x).sum();
			CountryWithDeathCasesDTO countryWithDeathCasesDTO = new CountryWithDeathCasesDTO();
			countryWithDeathCasesDTO.setCountry(country);
			countryWithDeathCasesDTO.setTotalDeathCountToday(totalDeathToday);
			countryWithDeathCasesDTO.setTotalDeathCountPreviousDay(totalDeathPreviousDay);

			countryWithDeathCases.add(countryWithDeathCasesDTO);

		});
		return countryWithDeathCases;
	}

	public int totalCountIncrease() {
		List<CountryWithCasesDTO> countryWiseData = getCountryWithCases();
		int increaseCases = countryWiseData.stream().mapToInt(x -> x.getPresentDayCases()).sum()
				- countryWiseData.stream().mapToInt(x -> x.getPreviousDayCases()).sum();
		return increaseCases;
	}

	public int totalDeathCases() {
		List<CountryWithDeathCasesDTO> countryWithDeathCaseeData = getCountryWithDeathCases();
		int totalDeathCount = countryWithDeathCaseeData.stream().mapToInt(x -> x.getTotalDeathCountToday()).sum();
		return totalDeathCount;
	}

	public CountryWithCasesDTO getDataOfSpecificCountry(String countryName) {
		LocationStats data = locationStatsDAO.getDataOfGivenCountry(countryName);
		CountryWithCasesDTO countryWithCasesDTO = new CountryWithCasesDTO();
		countryWithCasesDTO.setCountryName(data.getCountry());
		countryWithCasesDTO.setPresentDayCases(data.getLatestTotalCases());
		countryWithCasesDTO.setPreviousDayCases(data.getPreviousTotalCases());

		return countryWithCasesDTO;
	}

	public List<AllDataCountryWiseDTO> allData() {
		List<AllDataCountryWiseDTO> allData = new ArrayList<>();
		Set<String> allCountries = locationStatsDeathDAO.allCountriesDeath().stream().collect(Collectors.toSet());

		allCountries.forEach(country -> {
			AllDataCountryWiseDTO allDataCountryWiseDTO = new AllDataCountryWiseDTO();
			allDataCountryWiseDTO.setCountry(country);
			allDataCountryWiseDTO.setPresentDayCases(
					locationStatsDAO.totalPresentCaseOfCountry(country).stream().mapToInt(x -> x).sum());
			allDataCountryWiseDTO.setPreviousDayCases(
					locationStatsDAO.totalPreviousCaseOfCountry(country).stream().mapToInt(x -> x).sum());
			allDataCountryWiseDTO.setTotalDeathCountToday(
					locationStatsDeathDAO.listOfDeathReportedCountryWiseToday(country).stream().mapToInt(x -> x).sum());
			allDataCountryWiseDTO.setTotalDeathCountPreviousDay(locationStatsDeathDAO
					.listOfDeathReportedCountryWisePrevious(country).stream().mapToInt(x -> x).sum());

			allData.add(allDataCountryWiseDTO);

		});

		return allData;
	}

	public int totalInfectedPatient() {
		List<IndiaCoronadata> allData = (List<IndiaCoronadata>) indiaCoronadataDAO.findAll();
		System.out.println(allData.size());
		List<IndiaCoronadata> allPatient = allData.stream()
				.filter(patient -> patient.getCurrentstatus().equals("") == false).collect(Collectors.toList());
		System.out.println("total patient : " + allPatient.size());
		return allPatient.size();

	}

	public List<IndiaCoronadata> getData() {
		List<IndiaCoronadata> allData = (List<IndiaCoronadata>) indiaCoronadataDAO.findAll();
		System.out.println(allData.size());
		List<IndiaCoronadata> allPatient = allData.stream()
				.filter(patient -> patient.getCurrentstatus().equalsIgnoreCase("Hospitalized")
						&& patient.getCurrentstatus().equalsIgnoreCase("Recovered"))
				.collect(Collectors.toList());
		return allPatient;
	}

	public List<IndiaCoronadata> casesByState(String stateName) {
		List<IndiaCoronadata> allData = (List<IndiaCoronadata>) indiaCoronadataDAO.findAll();
		List<IndiaCoronadata> dataByState = allData.stream()
				.filter(patient -> patient.getDetectedstate().equalsIgnoreCase(stateName)).collect(Collectors.toList());

		return dataByState;
	}

	// @PostConstruct
	public Map<String, Double> ageGroupPresent() {
		Map<String, Double> ageGroup = new HashMap<>();
		List<IndiaCoronadata> allData = (List<IndiaCoronadata>) indiaCoronadataDAO.findAll();
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int m = 0;
		int n = 0;
		int o = 0;
		int totalCasesWithAgeIdentified = 0;
		for (IndiaCoronadata patient : allData) {
			if (!patient.getAgebracket().equals("") && patient.getAgebracket().toCharArray().length < 3) {
				totalCasesWithAgeIdentified = totalCasesWithAgeIdentified + 1;

				if (Integer.parseInt(patient.getAgebracket()) >= 0 && Integer.parseInt(patient.getAgebracket()) <= 14) {
					i = i + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 15
						&& Integer.parseInt(patient.getAgebracket()) <= 24) {
					j = j + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 25
						&& Integer.parseInt(patient.getAgebracket()) <= 34) {
					k = k + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 35
						&& Integer.parseInt(patient.getAgebracket()) <= 44) {
					l = l + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 45
						&& Integer.parseInt(patient.getAgebracket()) <= 54) {
					m = m + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 55
						&& Integer.parseInt(patient.getAgebracket()) <= 64) {
					n = n + 1;
				} else if (Integer.parseInt(patient.getAgebracket()) >= 65) {
					o = o + 1;
				}
			}
		}

		ageGroup.put(InterfaceConstants.ZERO_TO_FOURTEEN, mathCalculation.percent(totalCasesWithAgeIdentified, i));
		ageGroup.put(InterfaceConstants.FIFTEEN_TO_TWENTYFOUR, mathCalculation.percent(totalCasesWithAgeIdentified, j));
		ageGroup.put(InterfaceConstants.TWENTYFIVE_TO_THIRTYFOUR,
				mathCalculation.percent(totalCasesWithAgeIdentified, k));
		ageGroup.put(InterfaceConstants.THIRTYFIVE_TO_FORTYFOUR,
				mathCalculation.percent(totalCasesWithAgeIdentified, l));
		ageGroup.put(InterfaceConstants.FORTYFIVE_TO_FIFTYFOUR,
				mathCalculation.percent(totalCasesWithAgeIdentified, m));
		ageGroup.put(InterfaceConstants.FIFTYFIVE_TO_SIXTYFOUR,
				mathCalculation.percent(totalCasesWithAgeIdentified, n));
		ageGroup.put(InterfaceConstants.SIXTYFIVE_AND_ABOVE, mathCalculation.percent(totalCasesWithAgeIdentified, o));
		System.out.println(ageGroup);

		return ageGroup;
	}

	@PostConstruct
	public Map<String, Integer> arrangedByCatg() {
		Map<String, Integer> patientBycat = new HashMap<>();
		List<IndiaCoronadata> allData = (List<IndiaCoronadata>) indiaCoronadataDAO.findAll();
		for (IndiaCoronadata patient : allData) {
			if (patient.getCurrentstatus().equals("")) {
				patient.setCurrentstatus("UNKNOWN");
			}
			if (patientBycat.containsKey(patient.getCurrentstatus())) {
				patientBycat.put(patient.getCurrentstatus(), patientBycat.get(patient.getCurrentstatus()) + 1);
			} else {
				patientBycat.put(patient.getCurrentstatus(), 1);
			}
		}
		System.out.println(patientBycat);

		return patientBycat;
	}

	// FeignClient Call.

	@HystrixCommand(fallbackMethod = "getFallbackdataViaFeignCleint", commandProperties = {

			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5060") })
	public String dataViaFeignCleint() {
		List<String> patientStatus = new ArrayList<>();
		DataDTO dataDTO = covidDataClient.allPatientData();
		dataDTO.getRaw_data().forEach(patient -> {
			System.out.println(patient.getCurrentstatus());
			patientStatus.add(patient.getCurrentstatus());
		});
		return "{ \"command\" : \"working\"}"; // Just to get the JSON respond.
	}

	public String getFallbackdataViaFeignCleint() {
		System.out.println("Fallback METHOD");
		return "In Fallback method";
	}

	public List<IndiaCoronadata> getDataByState(String state) {
		// String state1 = "Odisha";
		System.out.println(indiaCoronadataDAO.getDataByState(state));
		return indiaCoronadataDAO.getDataByState(state);
	}

}
