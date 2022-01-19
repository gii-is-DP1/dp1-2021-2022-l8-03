package org.springframework.samples.upstream.piece.exceptions;

public class RoundNotInCourseException extends Exception{
	
    public RoundNotInCourseException() {
        super("The round is not in course");
    }
}
