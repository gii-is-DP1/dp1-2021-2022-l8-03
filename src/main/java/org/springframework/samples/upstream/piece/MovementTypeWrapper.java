package org.springframework.samples.upstream.piece;

public class MovementTypeWrapper {
	private Piece piece;
	private Boolean movementType;
	
	public MovementTypeWrapper(Piece piece, Boolean movementType) {
		this.piece = piece;
		this.movementType = movementType;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Boolean getMovementType() {
		return movementType;
	}

	public void setMovementType(Boolean movementType) {
		this.movementType = movementType;
	}
}
