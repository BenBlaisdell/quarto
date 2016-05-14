package game.traits;

public enum ShapeTrait implements Trait {
	SQUARE,
	CIRCLE;

	@Override
	public int getType() {
		return 0;
	}
}
