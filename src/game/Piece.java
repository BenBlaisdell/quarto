package game;

import game.traits.CenterTrait;
import game.traits.ColorTrait;
import game.traits.HeightTrait;
import game.traits.ShapeTrait;
import game.traits.Trait;

/**
 * A representation of one of 16 possible pieces in Quarto
 * 
 * @author Ben
 */
public class Piece {
	
	public final CenterTrait center;
	public final ColorTrait color;
	public final HeightTrait height;
	public final ShapeTrait shape;
	
	public final Trait[] traits;
	
	public final byte byteRepresentation;
	
	public Piece(CenterTrait center, ColorTrait color, HeightTrait height, ShapeTrait shape) {
		
		this.center 	= center;
		this.color 		= color;
		this.height 	= height;
		this.shape 		= shape;
		
		traits = new Trait[]{center, color, height, shape};
		
		byteRepresentation = createByteRepresentation();
		
	}
	
	/**
	 * @return A byte representation of this piece
	 */
	private byte createByteRepresentation() {
		byte centerBit 	= getBit(center);
		byte colorBit 	= getBit(color);
		byte heightBit 	= getBit(height);
		byte shapeBit 	= getBit(shape);
		
		byte representation = (byte) (centerBit | colorBit | heightBit | shapeBit);
		
		return representation;
	}
	
	/**
	 * Gets the bit representing the specific value of this trait shifted over
	 * by its index in the byte representation of a piece.
	 * 
	 * @param trait The <code>Trait</code> for which to get the relevant bit
	 * @return The shifted bit representing this trait
	 */
	private static byte getBit(Trait trait) {
		return (byte) (trait.ordinal() << trait.getType());
	}
	
	/**
	 * A byte representation of this <code>Piece</code> of the form: <br>
	 * [center] [color] [height] [shape] <br>
	 * Bit values are determined by the <code>Trait</code>'s ordinal number.
	 * 
	 * @return A byte representation of this piece
	 */
	public byte getByteRepresentation() {
		return byteRepresentation;
	}
}
