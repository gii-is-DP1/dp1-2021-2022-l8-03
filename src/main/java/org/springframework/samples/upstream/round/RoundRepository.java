package org.springframework.samples.upstream.round;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundRepository extends CrudRepository<Round, Integer> {
	
	public Collection<Round> findAll() throws DataAccessException;
	
	@Query("SELECT DISTINCT round FROM Round round WHERE round.id=:id")
	public Round findById(@Param("id") int id);
	
	@Query("SELECT r FROM Round r WHERE r.player.id=:player_id")
	public Collection<Round> findRoundByPlayerId(@Param("player_id") int player_id);
	
	@Query("SELECT DISTINCT round FROM Round round WHERE round.round_state=0")
    public Collection<Round> findCreatedRounds();

    @Query("SELECT DISTINCT round FROM Round round WHERE round.round_state=1")
    public Collection<Round> findInCourseRounds();

    @Query("SELECT DISTINCT round FROM Round round WHERE round.round_state=2")
    public Collection<Round> findFinishedRounds();
	
}
