package structs;

public class GameCharacter {
	public final String ID;
	public final CharacterBooleans bools;
	public final CharacterFloats floats;
	public GameCharacter(String iD) {
		super();
		ID = iD;
		bools = new CharacterBooleans();
		floats = new CharacterFloats(ID);
	}
}
