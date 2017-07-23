package run;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import structs.GameCharacter;

public class ClientSender implements Runnable{

	public final ArrayList<GameCharacter> characters;
	public final ArrayList<GameCharacter> myCharactersCharacters;
	public final ArrayList<GameCharacter> newOffline = new ArrayList<GameCharacter>();
	public final ArrayList<GameCharacter> newOnline = new ArrayList<GameCharacter>();
	public final Socket client;
	public final GameCharacter myCharacter;
	public final PrintWriter writer;
	
	public ClientSender(Socket client, GameCharacter myCharacter, ArrayList<GameCharacter> characters, PrintWriter writer) {
		super();
		this.characters = characters;
		this.myCharactersCharacters = new ArrayList<GameCharacter>(characters);
		this.client = client;
		this.myCharacter = myCharacter;
		this.writer = writer;
			
		for(GameCharacter character : myCharactersCharacters){
			String toWrite = character.ID;
			if(!toWrite.equals(myCharacter.ID)){
					
				toWrite += ", ";
				toWrite += character.floats.toString(2,4);
				System.out.println(toWrite);
					
				writer.println(toWrite);
				writer.flush();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
			
		characters.add(myCharacter);
	}


	@Override
	public void run() {
		int count=0;
		while(true){ 
			System.out.println("Sender");
			try {
				for(GameCharacter character : myCharactersCharacters){
					String toWrite = character.ID;
					if(!toWrite.equals(myCharacter.ID)){
						
						toWrite += ", ";
						toWrite += character.bools.toString()+", ";
						
						if(count > 1000){
							count -= 1000;
							toWrite += character.floats.toString(4);
						}else
						{
							toWrite += character.floats.toString(2);
						}
						
						
						writer.println(toWrite);
						writer.flush();
						Thread.sleep(50);
						count+=50;
					}
				
				}
				
				for(GameCharacter c : characters)
				{
					if(!c.equals(myCharacter))
					if(!myCharactersCharacters.contains(c)){
						newOnline.add(c);
					}
				}
				for(GameCharacter c : myCharactersCharacters){
					if(!c.equals(myCharacter))
					if(!characters.contains(c)){
						newOffline.add(c);
					}
				}
				if(!newOnline.isEmpty()){
					int i = newOnline.size()-1;
					writer.println(newOnline.get(i).ID);
					myCharactersCharacters.add(newOnline.get(i));
					newOnline.remove(i);
				}else if(!newOffline.isEmpty()){
					int i = newOffline.size()-1;
					writer.println(newOffline.get(i).ID);
					myCharactersCharacters.remove(newOffline.get(i));
					newOffline.remove(i);
				}
				
				Thread.sleep(50);
				count+=50;
				
				
				
			}catch(Exception e){
				
			} finally {
				writer.close();
			}
		}
	}

}
