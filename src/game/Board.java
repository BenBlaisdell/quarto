package game;

public class Board {

	public Piece[] pieces;
	public char filled;
	public long state;
	
	public Board() {
		pieces = new Piece[16];
		filled = 0;
		state = 0;
	}
	
	public void putPiece(Piece piece, int reverseIndex) {		
		pieces[reverseIndex] = piece;
		
		filled |= 1 << reverseIndex;
		state |= ((long) piece.getByteRepresentation()) << (reverseIndex * 4);
	}
	
	public Piece removePiece(int reversedIndex) {
		Piece piece = pieces[reversedIndex];
		pieces[reversedIndex] = null;
		
		filled &= ~(1 << reversedIndex);
		state &= ~(0xFL << (reversedIndex * 4));
		
		return piece;
	}
	
	public Piece getPiece(int reverseIndex) {
		return pieces[reverseIndex];
	}

	
}
