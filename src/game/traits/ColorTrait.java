package game.traits;

public enum ColorTrait implements Trait {
	LIGHT,
	DARK;

	@Override
	public int getType() {
		return 2;
	}
}
