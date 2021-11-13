package org.springframework.samples.upstream.tile;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RapidsTile extends Tile{
	private Integer orientation;
}
