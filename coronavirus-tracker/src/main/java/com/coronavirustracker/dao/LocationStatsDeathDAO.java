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

import com.coronavirustracker.model.LocationStatsDeath;

/**
 * @author sidharthdas
 *
 */

@Repository
@Transactional
public interface LocationStatsDeathDAO extends CrudRepository<LocationStatsDeath, Long> {

	@Query(value = "SELECT * FROM LOC_STATS_DEATH WHERE COUNTRY = :country AND STATE = :state", nativeQuery = true)
	List<LocationStatsDeath> getDataOfSpecificCountryAndState(@Param("country") String country,
			@Param("state") String state);

	@Modifying
	@Query(value = "UPDATE LOC_STATS_DEATH SET DEATH_PRESENT_DAY = :deathPresentDay, DEATH_PREVIOUS_DAY = :deathPreviousDay WHERE COUNTRY = :country AND STATE = :state", nativeQuery = true)
	public int updateData(@Param("country") String country, @Param("state") String state,
			@Param("deathPresentDay") int deathPresentDay, @Param("deathPreviousDay") int deathPreviousDay);

	@Query(value = "SELECT COUNTRY FROM LOC_STATS_DEATH", nativeQuery = true)
	public List<String> allCountriesDeath();

	@Query(value = "SELECT DEATH_PRESENT_DAY FROM LOC_STATS_DEATH WHERE COUNTRY = :country", nativeQuery = true)
	public List<Integer> listOfDeathReportedCountryWiseToday(@Param("country") String country);

	@Query(value = "SELECT DEATH_PREVIOUS_DAY FROM LOC_STATS_DEATH WHERE COUNTRY = :country", nativeQuery = true)
	public List<Integer> listOfDeathReportedCountryWisePrevious(@Param("country") String country);
}
