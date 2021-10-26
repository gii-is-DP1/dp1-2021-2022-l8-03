package org.springframework.samples.petclinic.round;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RoundRepository extends Repository<Round, Integer> {
	
	void save(Round round) throws DataAccessException;
	
	@Query("SELECT DISTINCT round FROM Round round")
	public Collection<Round> findAll();
	
	@Query("SELECT DISTINCT round FROM Round round WHERE round.id=:id")
	public Round findById(@Param("id") int id);
}
