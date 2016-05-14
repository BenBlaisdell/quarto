package game.traits;

/**
 * An interface implemented by the four traits that <code>Piece</code>s have
 * 
 * @author Ben
 */
public interface Trait {
		
	public final static int NUM_TRAIT_SUBTYPES = 2;
	
	public final static int NUM_TRAIT_TYPES = 4;

	public int getType();
	
	/**
	 * @return The ordinal of the enum value held by this trait.
	 * All implementing classes are enums, so they already have this method.
	 */
	public int ordinal();
		
}
