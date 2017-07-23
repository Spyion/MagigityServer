package run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import connections.mysqlconn;
import structs.GameCharacter;

public class ClientHandler implements Runnable {
	
	public final ArrayList<GameCharacter> characters;
	public final Socket client;
	public static ExecutorService listeners = Executors.newCachedThreadPool();
	public static ExecutorService writers = Executors.newCachedThreadPool();
	public ClientHandler(Socket client, ArrayList<GameCharacter> characters) {
		this.client = client;
		this.characters = characters;
	}
	@Override
	public void run() {
		try{
		PrintWriter writer = new PrintWriter(client.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		boolean disconnected = true;
		
		String s;
		while((s = reader.readLine()) != null){
			
			String[] strs = s.split(",");
			for(int i = 0; i < strs.length; i++){
				strs[i] = strs[i].trim();
			}
			if(mysqlconn.checkPw(strs[0], strs[1]).equals("true")){
				disconnected = false;
				GameCharacter character = new GameCharacter(strs[0]);
				writers.execute(new ClientSender(client, character, characters, writer));
				listeners.execute(new ClientReciever(client, character, characters, reader));
				System.out.println(strs[0]+" logged in");
				break;
			}
			 
		}
		if(disconnected){
		writer.close();
		reader.close();
		}
		}catch(Exception e){
		}
	}

}
