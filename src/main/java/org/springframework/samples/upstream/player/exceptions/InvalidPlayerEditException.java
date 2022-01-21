package org.springframework.samples.upstream.player.exceptions;

public class InvalidPlayerEditException extends Exception {

	public InvalidPlayerEditException() {
		super("You can only edit your own profile");
	}
	
}
