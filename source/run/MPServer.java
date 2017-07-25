package run;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import listeners.NetworkListener;
import packets.CharacterBooleans;
import packets.CharacterInts;
import packets.CharacterShorts;
import packets.CharactersRequest;
import packets.LoginAnswer;
import packets.LoginRequest;
import packets.News;
import packets.Offline;
import packets.Online;
import structs.GameCharacter;

public class MPServer {
	private Server server;
	
	public MPServer(){
		
		final int PORT = 25643;
		
		server = new Server();
		registerPackets();
		server.addListener(new NetworkListener().init(server));
		try {
			server.bind(PORT, PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		server.start();
	}
	
	
	
	private void registerPackets(){
		Kryo kryo = server.getKryo();
		kryo.register(CharacterBooleans.class);
		kryo.register(CharacterShorts.class);
		kryo.register(LoginAnswer.class);
		kryo.register(LoginRequest.class);
		kryo.register(News.class);
		kryo.register(Online.class);
		kryo.register(Offline.class);
		kryo.register(String[].class);
		kryo.register(CharactersRequest.class);
	}
	public static void main(String[] args){
		MPServer server = new MPServer();
		Log.set(Log.LEVEL_DEBUG);
		while(true);
	}
}
