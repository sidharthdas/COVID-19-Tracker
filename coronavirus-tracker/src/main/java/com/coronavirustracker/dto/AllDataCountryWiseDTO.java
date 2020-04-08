/**
 * 
 */
package com.coronavirustracker.dto;

/**
 * @author sidharthdas
 *
 */
public class AllDataCountryWiseDTO {

	private String country;
	private int presentDayCases;
	private int previousDayCases;
	private int totalDeathCountToday;
	private int totalDeathCountPreviousDay;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
