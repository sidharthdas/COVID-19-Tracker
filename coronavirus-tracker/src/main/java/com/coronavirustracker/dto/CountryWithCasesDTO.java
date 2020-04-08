/**
 * 
 */
package com.coronavirustracker.dto;

/**
 * @author sidharthdas
 *
 */
public class CountryWithCasesDTO {

	private String countryName;
	private int presentDayCases;
	private int previousDayCases;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getPresentDayCases() {
		return presentDayCases;
	}

	public void setPresentDayCases(int presentDayCases) {
		this.presentDayCases = presentDayCases;
	}

	public int getPreviousDayCases() {
		return previousDayCases;
	}

	public void setPreviousDayCases(int previousDayCases) {
		this.previousDayCases = previousDayCases;
	}

}
