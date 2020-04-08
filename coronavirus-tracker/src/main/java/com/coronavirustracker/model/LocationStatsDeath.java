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
@Entity(name = "LOC_STATS_DEATH")
@Table(name = "LOC_STATS_DEATH")
public class LocationStatsDeath {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "STATE")
	private String state;

	@Column(name = "DEATH_PRESENT_DAY")
	private int totalNoOfDeathPresentDay;

	@Column(name = "DEATH_PREVIOUS_DAY")
	private int totalNoOfDeathPreviousDay;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTotalNoOfDeathPresentDay() {
		return totalNoOfDeathPresentDay;
	}

	public void setTotalNoOfDeathPresentDay(int totalNoOfDeathPresentDay) {
		this.totalNoOfDeathPresentDay = totalNoOfDeathPresentDay;
	}

	public int getTotalNoOfDeathPreviousDay() {
		return totalNoOfDeathPreviousDay;
	}

	public void setTotalNoOfDeathPreviousDay(int totalNoOfDeathPreviousDay) {
		this.totalNoOfDeathPreviousDay = totalNoOfDeathPreviousDay;
	}

}
