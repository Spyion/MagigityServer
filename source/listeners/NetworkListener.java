package listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import connections.mysqlconn;
import packets.Attack;
import packets.CharacterShorts;
import packets.CharactersRequest;
import packets.DrawWeapon;
import packets.LoginAnswer;
import packets.LoginRequest;
import packets.News;
import packets.Offline;
import packets.Online;
import structs.GameCharacter;
import tools.BidirectionalMap;
import tools.BoolCoder;

public class NetworkListener extends Listener{
	
	private Server server;
	private BidirectionalMap<GameCharacter, Integer> characterIDs;
	private List<Integer> connected = Collections.synchronizedList(new ArrayList<Integer>());
	private Set<GameCharacter> onlineCharacters;
	public NetworkListener init(Server server){
		this.server = server;
		
		characterIDs = new BidirectionalMap<GameCharacter, Integer>();
		onlineCharacters = characterIDs.getKeySet();
		
		
		return this;
	}
	@Override
	public void connected(Connection connection) {
		Log.info(connection.toString()+" has Connected");
		super.connected(connection);
	}
	@Override
	public void disconnected(Connection connection) {
		GameCharacter c = characterIDs.getKey(connection.getID());
		if(c!=null)
		{
			server.sendToAllExceptTCP(connection.getID(), new Offline(c.ID));
			GameCharacter.IDs.remove(new Byte(c.ID));
			onlineCharacters.remove(c);
			Log.info(c.name+"("+c.ID+")"+" logged out");
		}else{
			Log.info(connection.toString()+" has Disconnected");
		}
		connected.remove(new Integer(connection.getID()));
		super.disconnected(connection);
	}
	@Override
	public void received(Connection connection, Object object) {
		GameCharacter character = characterIDs.getKey(connection.getID());
		
		if(character == null){
			if(!connected.contains(connection.getID())){
			server.sendToTCP(connection.getID(), new News());
			connected.add(new Integer(connection.getID()));
			}
			
			if(object instanceof LoginRequest){
				LoginRequest login = (LoginRequest) object;
				LoginAnswer answer;
				if(mysqlconn.checkPw(login.username, login.password).equals("true")){
					GameCharacter c = new GameCharacter(login.username);
					answer = new LoginAnswer(true, c.ID);
					Log.info(c.name+"("+c.ID+")"+ " logged in");
					characterIDs.put(c, connection.getID());
					server.sendToAllExceptTCP(connection.getID(), new Online(c.ID, c.name, c.positionX, c.positionY, BoolCoder.encode(c.isWeaponDrawn)));
				}else{
					answer = new LoginAnswer();
				}
				
				
				server.sendToTCP(connection.getID(), answer);
			}
		}else{
			if(object instanceof CharacterShorts){				
				server.sendToAllExceptUDP(connection.getID(), object);
				CharacterShorts s = (CharacterShorts) object;
				character.positionX = s.positionX;
				character.positionY = s.positionY;
				character.rotation = s.rotation;
	
			}else if(object instanceof Attack){
				server.sendToAllExceptUDP(connection.getID(), object);

			}else if(object instanceof DrawWeapon){
				server.sendToAllExceptUDP(connection.getID(), object);
				DrawWeapon w = (DrawWeapon) object;
				if(BoolCoder.decode(w.drawn)[0])
					character.isWeaponDrawn = true;
				else
					character.isWeaponDrawn = false;
			}
			else if(object instanceof CharactersRequest){
				synchronized (onlineCharacters) {
					for(GameCharacter c : onlineCharacters)
						if(c.ID != character.ID)
							server.sendToTCP(connection.getID(), new Online(c.ID, c.name, c.positionX, c.positionY, BoolCoder.encode(c.isWeaponDrawn)));
				}
			}
			super.received(connection, object);
		}
	}

	
}
