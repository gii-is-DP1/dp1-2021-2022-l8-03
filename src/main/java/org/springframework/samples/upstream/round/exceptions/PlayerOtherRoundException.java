package org.springframework.samples.upstream.round.exceptions;

public class PlayerOtherRoundException extends Exception{
	public PlayerOtherRoundException() {
		super("You are in a round");
	}
}
