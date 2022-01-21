package org.springframework.samples.upstream.round.exceptions;

public class NotYourRoundException extends Exception{
	public NotYourRoundException() {
		super("You aren't in this round");
	}
}
