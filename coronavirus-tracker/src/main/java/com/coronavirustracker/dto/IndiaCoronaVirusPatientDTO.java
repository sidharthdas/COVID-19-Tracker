/**
 * 
 */
package com.coronavirustracker.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sidharthdas
 *
 */
@Getter
@Setter
public class IndiaCoronaVirusPatientDTO {

	private String agebracket;
	private String backupnotes;
	private String contractedfromwhichpatientsuspected;
	private String currentstatus;
	private String dateannounced;
	private String detectedcity;
	private String detecteddistrict;
	private String detectedstate;
	private String estimatedonsetdate;
	private String gender;
	private String nationality;
	private String notes;
	private String patientnumber;
	private String source1;
	private String source2;
	private String source3;
	private String statecode;
	private String statepatientnumber;
	private String statuschangedate;
	private String typeoftransmission;

	@Override
	public String toString() {
		return "IndiaCoronaVirusPatientDTO [agebracket=" + agebracket + ", backupnotes=" + backupnotes
				+ ", contractedfromwhichpatientsuspected=" + contractedfromwhichpatientsuspected + ", currentstatus="
				+ currentstatus + ", dateannounced=" + dateannounced + ", detectedcity=" + detectedcity
				+ ", detecteddistrict=" + detecteddistrict + ", detectedstate=" + detectedstate
				+ ", estimatedonsetdate=" + estimatedonsetdate + ", gender=" + gender + ", nationality=" + nationality
				+ ", notes=" + notes + ", patientnumber=" + patientnumber + ", source1=" + source1 + ", source2="
				+ source2 + ", source3=" + source3 + ", statecode=" + statecode + ", statepatientnumber="
				+ statepatientnumber + ", statuschangedate=" + statuschangedate + ", typeoftransmission="
				+ typeoftransmission + "]";
	}

}
