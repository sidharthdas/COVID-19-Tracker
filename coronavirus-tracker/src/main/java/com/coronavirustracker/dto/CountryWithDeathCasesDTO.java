/**
 * 
 */
package com.coronavirustracker.dto;

/**
 * @author sidharthdas
 *
 */
public class CountryWithDeathCasesDTO {

	private String country;
	private int totalDeathCountToday;
	private int totalDeathCountPreviousDay;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getTotalDeathCountToday() {
		return totalDeathCountToday;
	}

	public void setTotalDeathCountToday(int totalDeathCountToday) {
		this.totalDeathCountToday = totalDeathCountToday;
	}

	public int getTotalDeathCountPreviousDay() {
		return totalDeathCountPreviousDay;
	}

	public void setTotalDeathCountPreviousDay(int totalDeathCountPreviousDay) {
		this.totalDeathCountPreviousDay = totalDeathCountPreviousDay;
	}

}
