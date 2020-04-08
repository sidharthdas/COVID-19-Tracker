/**
 * 
 */
package com.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coronavirustracker.dto.AllDataCountryWiseDTO;
import com.coronavirustracker.dto.CountryWithCasesDTO;
import com.coronavirustracker.service.CoronavirusDataService;

/**
 * @author sidharthdas
 *
 */
@RestController
public class HomeRestController {

	@Autowired
	private CoronavirusDataService coronavirusDataService;

	@GetMapping("/country-cases")
	public ResponseEntity<List<CountryWithCasesDTO>> getCountryWiseCases() {
		return new ResponseEntity<List<CountryWithCasesDTO>>(coronavirusDataService.getCountryWithCases(),
				HttpStatus.OK);
	}

	@GetMapping("/increase-cases")
	public ResponseEntity<Integer> increaseCount() {
		return new ResponseEntity<Integer>(coronavirusDataService.totalCountIncrease(), HttpStatus.OK);
	}

	@GetMapping("/all-data")
	public ResponseEntity<List<AllDataCountryWiseDTO>> getAllData() {
		return new ResponseEntity<List<AllDataCountryWiseDTO>>(coronavirusDataService.allData(), HttpStatus.OK);
	}

	@GetMapping("/total-death-count")
	public int totalCount() {
		return coronavirusDataService.totalDeathCases();
	}

}
