package structs;

import connections.mysqlconn;

public class CharacterFloats {
	public float posX;
	public float posY;
	public float rot;
	public float cameraRot;
	
	public void read(String[] vals, int start) throws NumberFormatException{
//		for(String val : vals){
//			val.trim();
//		}
		if(vals.length > start && vals.length > 0){
			rot = Float.parseFloat(vals[start++]);
		}if(vals.length > start && vals.length > 1){
			cameraRot = Float.parseFloat(vals[start++]);
		}if(vals.length > start && vals.length > 2){
			posX = Float.parseFloat(vals[start++]);
		}if(vals.length > start && vals.length > 3){
			posY = Float.parseFloat(vals[start++]);
		}
	}
	
	public String toString(int start, int length){
		String str = "";
		if(length>0&&start<1)
			str += Float.toString(rot)+", ";
		if(length>1&&start<2)
			str += Float.toString(cameraRot)+", ";
		if(length>2&&start<3)
			str += Float.toString(posX)+", ";
		if(length>3&&start<4)
			str += Float.toString(posY)+", ";
		
		
		return str.substring(0, str.length()-2);
	}
	public String toString(int length){
		return toString(0, length);
	}
	public CharacterFloats(String ID){
		posX = Float.parseFloat(mysqlconn.getData(ID, "posX"));
		posY = Float.parseFloat(mysqlconn.getData(ID, "posY"));
	}
}
