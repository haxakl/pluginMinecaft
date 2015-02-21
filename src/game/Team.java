package game;

import java.util.ArrayList;
import org.bukkit.entity.Player;

/**
 * Model Team
 * @author Guillaume
 */
public class Team {
    
    private ArrayList<Player> players;
    
    public String name = "";
    
    public Team() {
        players = new ArrayList<>();
    }
    
    /**
     * Ajoute un joueur à une équipe
     * @param player Joueur à ajouter à l'équipe
     */
    public void addPlayer(Player player) {
        if(player != null) {
            players.add(player);
        }
    }
    
    /**
     * Supprime un joueur d'une équipe
     * @param player 
     */
    public void removePlayer(Player player) {
        if(player != null && players.contains(player)) {
            players.remove(player);
        }
    }
    
}
