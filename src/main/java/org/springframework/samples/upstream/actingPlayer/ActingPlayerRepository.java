package org.springframework.samples.upstream.actingPlayer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ActingPlayerRepository extends CrudRepository<ActingPlayer, Integer>{
	
	@Query("SELECT DISTINCT actingPlayer FROM ActingPlayer actingPlayer WHERE round_id=:id")
    public ActingPlayer findByRound(@Param("id") int id);

}
