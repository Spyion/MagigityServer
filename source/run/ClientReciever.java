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
		System.out.println("reader");

		try {
			while((s = reader.readLine()) != null){
				System.out.println("reader");
				String[] strs = s.split(",");
				for(int i = 0; i < strs.length; i++){
					strs[i] = strs[i].trim();
				}
				
				try{
					myCharacter.bools.read(strs[0]);
				}catch(NumberFormatException e){
					System.err.println(myCharacter.ID+": BOOLS ARE WRONG");
					client.close();
				}
				try{
					myCharacter.floats.read(strs, 1);
				}catch(NumberFormatException e){
					System.err.println(myCharacter.ID+":FLOATS ARE WRONG");

					client.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
