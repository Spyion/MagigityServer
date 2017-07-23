package run;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import debug.Debug;
import structs.GameCharacter;

public class Server {
	public static final int PORT = 6564;
	
	public static void main(String[] args){
		ServerSocket server = null;
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
				server = new ServerSocket(PORT);
				System.out.println("Server started");
				
				final ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();
				while(true){
					Socket client = server.accept();
					System.out.println("Connection recieved");
					executor.execute(new clientHandler(client, characters));
//					executor.execute(new Debug(characters));
				}		
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				System.exit(-1);
			}
			System.exit(-1);
		}
	}
}
