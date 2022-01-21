package org.springframework.samples.upstream.player.exceptions;

public class NoPermissionException extends Exception {
	
	public NoPermissionException() {
		super("Only administrators can access this function");
	}

}
