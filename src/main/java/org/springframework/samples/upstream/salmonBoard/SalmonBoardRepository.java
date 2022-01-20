package org.springframework.samples.upstream.salmonBoard;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface SalmonBoardRepository extends CrudRepository<SalmonBoard, Integer>{
	
	@Query("SELECT salmonboard FROM SalmonBoard salmonboard")
	public List<SalmonBoard> findAll();
	
	@Query("SELECT salmonboard FROM SalmonBoard salmonboard WHERE salmonboard.round.id=:round_id")
	public SalmonBoard findBoardInRound(@Param("round_id") int round_id);
}
