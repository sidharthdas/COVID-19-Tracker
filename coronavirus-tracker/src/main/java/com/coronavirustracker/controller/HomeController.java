/**
 * 
 */
package com.coronavirustracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coronavirustracker.service.CoronavirusDataService;

/**
 * @author sidharthdas
 *
 */
@Controller
public class HomeController {

	@Autowired
	private CoronavirusDataService coronavirusDataService;

	@GetMapping("/")
	public String test() {
		return "test";
	}

	@GetMapping("/home")
	public String home(Model model) {
		int totalCases = coronavirusDataService.getCountryWithCases().stream().mapToInt(x -> x.getPresentDayCases())
				.sum();
		int newDeathCases = coronavirusDataService.getCountryWithDeathCases().stream()
				.mapToInt(x -> x.getTotalDeathCountToday()).sum()
				- coronavirusDataService.getCountryWithDeathCases().stream()
						.mapToInt(x -> x.getTotalDeathCountPreviousDay()).sum();
		model.addAttribute("allData", coronavirusDataService.allData());
		model.addAttribute("totalReportedCases", totalCases);
		model.addAttribute("totalNewCases", coronavirusDataService.totalCountIncrease());
		model.addAttribute("india", coronavirusDataService.getDataOfSpecificCountry("India"));
		model.addAttribute("totalDeathCount", coronavirusDataService.totalDeathCases());
		model.addAttribute("newDeathReportedToday", newDeathCases);
		return "home";
	}

}
