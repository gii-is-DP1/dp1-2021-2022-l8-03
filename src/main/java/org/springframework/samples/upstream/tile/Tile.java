package org.springframework.samples.upstream.tile;

import javax.persistence.Entity;

import org.springframework.samples.upstream.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

//@MappedSuperclass
@Getter
@Setter
@Entity
public class Tile extends BaseEntity {
	private Integer rowIndex;
	private Integer columnIndex;
}


