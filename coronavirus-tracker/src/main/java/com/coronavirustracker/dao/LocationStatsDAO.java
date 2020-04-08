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

import com.coronavirustracker.model.LocationStats;

/**
 * @author sidharthdas
 *
 */
@Transactional
@Repository
public interface LocationStatsDAO extends CrudRepository<LocationStats, Long> {

	@Transactional(timeout = 4)
	@Query(value = "SELECT PRESENT_DAY_TOTAL_CASES FROM LOC_STATS WHERE COUNTRY= :country AND STATE= :state", nativeQuery = true)
	List<Integer> presentDayCases(@Param("country") String country, @Param("state") String state);

	@Transactional(timeout = 10)
	@Modifying
	@Query(value = "UPDATE LOC_STATS SET PRESENT_DAY_TOTAL_CASES = :presentDayCases, PREVIOUS_DAY_TOTAL_CASES = :previousDayCases WHERE COUNTRY = :country AND STATE = :state", nativeQuery = true)
	int updateCases(@Param("presentDayCases") int presentDayCases, @Param("previousDayCases") int previousDayCases,
			@Param("country") String country, @Param("state") String state);

	@Query(value = "SELECT COUNT(*) FROM LOC_STATS", nativeQuery = true)
	int totalCountOfRows();
	
	
	@Query(value = "SELECT * FROM LOC_STATS ORDER BY COUNTRY ASC", nativeQuery = true)
	List<LocationStats> getAllStats();
	
	@Query(value = "SELECT COUNTRY FROM LOC_STATS", nativeQuery = true)
	List<String> allCountry();	
	
	@Query(value = "SELECT PRESENT_DAY_TOTAL_CASES FROM LOC_STATS WHERE COUNTRY = :country", nativeQuery = true)
	List<Integer> totalPresentCaseOfCountry(@Param("country") String country);
	
	@Query(value = "SELECT PREVIOUS_DAY_TOTAL_CASES FROM LOC_STATS WHERE COUNTRY = :country", nativeQuery = true)
	List<Integer> totalPreviousCaseOfCountry(@Param("country") String country);
	
	@Query(value = "SELECT * FROM LOC_STATS WHERE COUNTRY = :country", nativeQuery = true)
	LocationStats getDataOfGivenCountry(@Param("country")String country);

}
