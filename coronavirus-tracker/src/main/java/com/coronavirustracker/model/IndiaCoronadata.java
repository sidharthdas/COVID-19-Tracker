package com.coronavirustracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sidharthdas
 *
 */
@Getter
@Setter
@Entity(name = "INDIA_COVID_DATA")
@Table(name = "INDIA_COVID_DATA")
public class IndiaCoronadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "AGE")
	private String agebracket;
	@Column(name = "BACKUP_NOTE")
	private String backupnotes;
	@Column(name = "REASON")
	private String contractedfromwhichpatientsuspected;
	@Column(name = "CURRENT_STATUS")
	private String currentstatus;
	@Column(name = "INFACTED_ON")
	private String dateannounced;
	@Column(name = "CITY")
	private String detectedcity;
	@Column(name = "DISTRICT")
	private String detecteddistrict;
	@Column(name = "STATE")
	private String detectedstate;
	@Column(name = "ESTIMATED_ON")
	private String estimatedonsetdate;
	@Column(name = "SEX")
	private String gender;
	@Column(name = "NATIONALITY")
	private String nationality;
	@Column(name = "NOTE")
	private String notes;
	@Column(name = "PATIENT_ID")
	private String patientnumber;
	@Column(name = "TRANSMISSION_TYPE")
	private String typeoftransmission;
}
