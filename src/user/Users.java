package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Users {
	
	/**
	 * List of users (including bot).
	 */
	
	private static ArrayList<Player> listOfUsers = new ArrayList<Player>();
	
	/**
	 * Initialize game by adding 3 ai bots to the game.
	 */
	
    public static void generate() {
    	listOfUsers.add(new Player("bot1",null,0,0,0,0,true));
    	listOfUsers.add(new Player("bot2",null,0,0,0,0,true));
    	listOfUsers.add(new Player("bot3",null,0,0,0,0,true));
    }
    
    /**
     * Get users from <b>users.txt</b> file and add them to list of users.
     */
    public static void loadLocalUsers() {  	
		try {
			// read file
			BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader("users.txt")));
			String line;
			// loop until last line
			while((line = reader.readLine()) != null) {
				// split user values by ","
				String[] playersValues = line.split(",");
				
				String nickname = playersValues[0];
				String imagePath = playersValues[1];
				
				File imageFile = new File("assets/" + playersValues[1]);
				
		        if (!imageFile.exists()) {
		        	try {
		        		// if user image doesn't exists copy the default avatar
		        		Files.copy(Paths.get("assets/default_avatar.png"), Paths.get("assets/" + imagePath), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
				
				int gamesPlayed = Integer.parseInt(playersValues[2]) ;
				int gamesWon = Integer.parseInt(playersValues[3]);
				int playerLevel = Integer.parseInt(playersValues[4]);
				int pointExperience = Integer.parseInt(playersValues[5]);
				
				listOfUsers.add(new Player(nickname,imagePath,gamesPlayed,gamesWon,playerLevel,pointExperience,false));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * 
     * Update <b>users.txt</b> file with new player values. Avatar is not included in update, there is a custom function for it in <b>EditProfileController.java</b>.
     * <br><br>
     * <b>newNickname</b> implementation is caused by the fact that changing player nickname 
     * and then searching the player in <b>users.txt</b> with it cause to get no results.
     * 
     * @param player Player to apply update
     * @param newNickname New nickname to update
     */
    
    public static void updateUserFile(Player player,String newNickname) {
    	try {
			// read users.txt
			BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader("users.txt")));
			ArrayList<String[]> textfile = new ArrayList<String[]>();
			String line;
			while((line = reader.readLine()) != null) {
				String[] playerValues = line.split(",");
				// Change only line matching the username
				if(playerValues[0].equals(player.getNickname())) {
					playerValues[0] = newNickname != null ? newNickname : player.getNickname();
					playerValues[1] = player.getAvatarFileName();
					// need to update statistics because the function is used in Game.java to update them
					playerValues[2] = Integer.toString(player.getGamesPlayed());
					playerValues[3] = Integer.toString(player.getGamesWon());
					playerValues[4] = Integer.toString(player.getPlayerLevel());
					playerValues[5] = Integer.toString(player.getPointExperience());
				}
				textfile.add(playerValues);
			}
			reader.close();
			// write (update) users.txt file
			BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
			textfile.forEach(l -> {
				try {
					writer.append(String.join(",", l));
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Get list of users.
     * @return List of users
     */
    public static ArrayList<Player> getListOfUsers(){
        return listOfUsers;
    }
}
