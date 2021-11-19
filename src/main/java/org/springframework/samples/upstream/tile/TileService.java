package org.springframework.samples.upstream.tile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerRepository;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TileService {
	@Autowired
	private TileRepository tileRepository;

	@Autowired
	private TileService tileService;

	@Autowired
	public TileService(TileRepository tileRepository) {
		this.tileRepository = tileRepository;
	}	

	@Transactional(readOnly = true)
	public Tile findTileById(int id) throws DataAccessException {
		return tileRepository.findById(id);
	}
	

}
