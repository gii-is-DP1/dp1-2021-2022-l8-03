package org.springframework.samples.upstream.piece.exceptions;

public class InvalidDirectionJumpException extends Exception {
	
	public InvalidDirectionJumpException() {
		super("You can't jump backwards nor in L form");
	}

}
