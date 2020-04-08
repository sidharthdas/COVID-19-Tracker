/**
 * 
 */
package com.coronavirustracker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.coronavirustracker.dto.DataDTO;

/**
 * @author sidharthdas
 *
 */

@FeignClient(value = "rawData", url = "https://api.covid19india.org/")
public interface CovidDataClient {

	@GetMapping("raw_data.json")
	DataDTO allPatientData();

}
