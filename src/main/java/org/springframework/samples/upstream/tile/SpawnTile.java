package org.springframework.samples.upstream.tile;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SpawnTile extends Tile{
	private Integer salmonEggs;
}
