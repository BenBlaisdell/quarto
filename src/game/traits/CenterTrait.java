package game.traits;

public enum CenterTrait implements Trait {
	HOLE,
	SOLID;

	@Override
	public int getType() {
		return 3;
	}
}
