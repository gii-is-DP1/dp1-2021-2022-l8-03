package org.springframework.samples.upstream.tile;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TileType {
	BEAR ("bear"),
	EAGLE ("eagle"),
	HERON ("heron"),
	WATERFALL ("waterfall"),
	WATER ("water"),
	SEA ("sea"),
	ROCK ("rock"),
	RAPIDS ("rapids"),
	SPAWN ("spawn");
	
	private final String code;
	
	private TileType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	@JsonValue
	public String toValue() {
		return getCode();
	}
}
