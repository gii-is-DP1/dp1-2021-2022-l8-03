package org.springframework.samples.upstream.score;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ScoreRepository extends CrudRepository<Score, Integer>{
			
	@Query("SELECT score FROM Score score WHERE player_id=:player_id0 AND round_id=:round_id0")
	public Score findByPlayerAndRound(@Param("player_id0") int player_id0, @Param("round_id0") int round_id0);
	
	@Query("SELECT s FROM Score s WHERE round_id=:round_id0 ORDER BY value DESC")
	public List<Score> findByRound(@Param("round_id0") int round_id0);
}
