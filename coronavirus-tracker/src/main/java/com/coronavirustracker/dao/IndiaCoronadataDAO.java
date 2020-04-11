/**
 * 
 */
package com.coronavirustracker.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.coronavirustracker.model.IndiaCoronadata;

/**
 * @author sidharthdas
 *
 */
@Repository
@Transactional
public interface IndiaCoronadataDAO extends CrudRepository<IndiaCoronadata, Long> {

	@Query(value = "SELECT * FROM INDIA_COVID_DATA WHERE PATIENT_ID = :patientId", nativeQuery = true)
	List<Object> checkPatient(@Param("patientId") String patientId);

	@Modifying
	@Query(value = "UPDATE INDIA_COVID_DATA SET CURRENT_STATUS = :currentStatus WHERE PATIENT_ID = :patientNumber", nativeQuery = true)
	int updatePatientDetails(String patientNumber, String currentStatus);

	@Query(value = "SELECT * FROM INDIA_COVID_DATA WHERE STATE = :STATE", nativeQuery = true)
	List<IndiaCoronadata> getDataByState(@Param("STATE") String state);

}
