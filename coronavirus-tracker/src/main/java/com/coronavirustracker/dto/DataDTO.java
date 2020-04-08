/**
 * 
 */
package com.coronavirustracker.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sidharthdas
 *
 */
public class DataDTO {
	@Getter
	@Setter
	private List<IndiaCoronaVirusPatientDTO> raw_data = new ArrayList<>();
	
	

}
