package packets;

import connections.mysqlconn;

public class CharacterInts {
	public int positionX;
	public int positionY;
	public final String ID;
	

	public CharacterInts(String ID){
		positionX = Integer.parseInt(mysqlconn.getData(ID, "posX"));
		positionY = Integer.parseInt(mysqlconn.getData(ID, "posY"));
		this.ID = ID;
	}
	public CharacterInts(){
		this.ID = null;
	}
	public void set(CharacterInts i){
		positionX = i.positionX;
		positionY = i.positionY;
	}
}
