package run;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int PORT = 4772;
	
	public static void main(){
		try {
			ServerSocket server = new ServerSocket(PORT);
			
			
			while(true){
				Socket client = server.accept();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
