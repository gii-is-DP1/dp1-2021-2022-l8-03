package org.springframework.samples.upstream.tile;


import javax.persistence.MappedSuperclass;

import org.springframework.samples.upstream.model.BaseEntity;



@MappedSuperclass

public class Tile extends BaseEntity {
	private Integer rowIndex;
	private Integer columnIndex;
}


