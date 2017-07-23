package run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import structs.GameCharacter;

public class ClientReciever implements Runnable{

	public final ArrayList<GameCharacter> characters;
	public final Socket client;
	public final GameCharacter myCharacter;
	public final BufferedReader reader;
	
	public ClientReciever(Socket client, GameCharacter myCharacter, ArrayList<GameCharacter> characters, BufferedReader reader) {
		super();
		this.characters = characters;
		this.client = client;
		this.myCharacter = myCharacter;
		this.reader = reader;
	}
	@Override
	public void run() {
		String s;

		try {
			while((s = reader.readLine()) != null){
				String[] strs = s.split(",");
				for(int i = 0; i < strs.length; i++){
					strs[i] = strs[i].trim();
				}
				
				try{
					myCharacter.bools.read(strs[0]);
				}catch(NumberFormatException e){
					System.err.print(myCharacter.ID+": BOOLS ARE WRONG: ");
					System.err.println(strs[0]);
					client.close();
				}
				try{
					myCharacter.floats.read(strs, 1);
				}catch(NumberFormatException e){
					System.err.println(myCharacter.ID+":FLOATS ARE WRONG");
					System.out.println(s);
					client.close();
				}
			}
		} catch (IOException e) {
			System.out.println(myCharacter.ID+" Disconnected");
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			characters.remove(myCharacter);
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
