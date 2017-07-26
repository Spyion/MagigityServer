package structs;

import java.util.ArrayList;

import connections.mysqlconn;

public class GameCharacter {
	public final String name;
	public final byte ID;
	
	public static final ArrayList<Byte> IDs = new ArrayList<Byte>();
	
	public int positionX;
	public int positionY;
	public boolean isWeaponDrawn;
	//in Degrees
	public float rotation;
	
	public GameCharacter(String name) {
		super();
		this.ID = findLowestID();
		this.name = name;
//		positionX = Integer.parseInt(mysqlconn.getData("name", "posX"));
//		positionY = Integer.parseInt(mysqlconn.getData("name", "posY"));
	}
	public GameCharacter(){
		ID = 0;
		name = null;
	}
	
	private synchronized static  byte findLowestID(){
		byte minVal = Byte.MIN_VALUE;
		for(Byte id : IDs)
			if(id==minVal){
				minVal++;
			}else{
				break;
			}

		IDs.add(minVal-Byte.MIN_VALUE, minVal);
		return minVal;
	}
}
