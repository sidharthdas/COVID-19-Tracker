/**
 * 
 */
package com.coronavirustracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sidharthdas
 *
 */
@Entity(name ="LOC_STATS")
@Table(name = "LOC_STATS")
public class LocationStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "PRESENT_DAY_TOTAL_CASES")
	private int latestTotalCases;

	@Column(name = "PREVIOUS_DAY_TOTAL_CASES")
	private int previousTotalCases;

	public int getPreviousTotalCases() {
		return previousTotalCases;
	}

	public void setPreviousTotalCases(int previousTotalCases) {
		this.previousTotalCases = previousTotalCases;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getLatestTotalCases() {
		return latestTotalCases;
	}

	public void setLatestTotalCases(int latestTotalCases) {
		this.latestTotalCases = latestTotalCases;
	}

	@Override
	public String toString() {
		return "LocationStats [state=" + state + ", country=" + country + ", latestTotalCases=" + latestTotalCases
				+ "]";
	}

}