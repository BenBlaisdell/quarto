package game.traits;

public enum HeightTrait implements Trait {
	TALL,
	SHORT;
	
	@Override
	public int getType() {
		return 1;
	}
}
