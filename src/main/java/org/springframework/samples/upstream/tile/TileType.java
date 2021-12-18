package org.springframework.samples.upstream.tile;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TileType {
	BEAR ("bear"),				//0
	EAGLE ("eagle"),			//1
	HERON ("heron"),			//2
	WATERFALL ("waterfall"),	//3
	WATER ("water"),			//4
	ROCK ("rock"),				//5
	RAPIDS ("rapids"),			//6
	SPAWN ("spawn"),			//7
	SEA ("sea");				//8
	
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
