package game;

import game.traits.CenterTrait;
import game.traits.ColorTrait;
import game.traits.HeightTrait;
import game.traits.ShapeTrait;
import game.traits.Trait;

public class TraitMap {

	int[][] traitTotals;
	Trait[] mapping;
	
	public TraitMap() {
		traitTotals = new int[Trait.NUM_TRAIT_TYPES][Trait.NUM_TRAIT_SUBTYPES];
	}
	
	/**
	 * Adds the <code>Trait</code>s of a <code>Piece</code> to <code>traitTotals</code>
	 * 
	 * @param piece The <code>Piece</code> whose <code>Trait</code>s will be added
	 */
	public void addPieceTraits(Piece piece) {
		for (Trait trait : piece.traits) {
			traitTotals[trait.getType()][trait.ordinal()]++;
		}
		mapping = null;
	}
	
	/**
	 * Gets a mapping for the given <code>Trait</code> in the form of <code>[index, bit]</code>. <code>index</code> is the given <code>Trait</code>'s 
	 * location in a 4 bit binary representation of a generalized <code>Piece</code>, with 0 being the LSB. <code>bit</code> is 1 or 0 depending on how 
	 * the <code>Trait</code> subtype should be marked in the representation based on the number of instances on the <code>Board</code>.
	 * 
	 * @param trait The <code>Trait</code> for which to get a mapping
	 * @return The mapping in the form of [index, bit]
	 */
	public int[] map(Trait trait) {
		if (mapping == null) // The number of each trait has been updated since the last mapping
			buildMapping();
		
		// Look through the mapping of Traits and find where the given type of Trait goes
		for (int traitIndex = mapping.length - 1; traitIndex >= 0; traitIndex--) {
			if (mapping[traitIndex].getType() == trait.getType()) {
				// If the specific subtype of this Trait is used in the mapping then it is the most common and is marked with a "1".
				int subtraitMapping = (trait.ordinal() == mapping[traitIndex].ordinal()) ? 1 : 0;
				return new int[]{traitIndex, subtraitMapping};
			}
		}
		
		return new int[]{-1, -1}; // Something went wrong in the mapping process
		
	}

	/**
	 * Builds <code>mapping</code> for use in mapping a specific subtrait to its proper index and bit representation
	 */
	private void buildMapping() {
		// Initialize the map with default traits
		// Traits 
		// This is the same ordering 
		mapping = new Trait[]{ShapeTrait.SQUARE, HeightTrait.TALL, ColorTrait.LIGHT, CenterTrait.HOLE};
		int[][] totalsCopy = getTotalsCopy();
		
		// Iterate through the spots for traits in the mapping, starting at the trait with least variation
		for (int mappingIndex = mapping.length - 1; mappingIndex >= 0; mappingIndex--);
			//setTraitInMap(totalsCopy, mappingIndex);
	}

	/**
	 * @return A copy of the current <code>traitTotals</code>
	 */
	private int[][] getTotalsCopy() {
		int[][] totalsCopy = new int[traitTotals.length][traitTotals[0].length];
		
		for (int traitIndex = 0; traitIndex < traitTotals.length; traitIndex++) {
			for (int subtypeIndex = 0; subtypeIndex < traitTotals[0].length; subtypeIndex++)
				totalsCopy[traitIndex][subtypeIndex] = traitTotals[traitIndex][subtypeIndex];
		}
		
		return totalsCopy;
	}

	
	
}
