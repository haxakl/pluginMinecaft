package listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import pluginminecraft.SpeedUHC;

/**
 *
 * @author Guillaume
 */
public class PlayerListener implements Listener {

    private final SpeedUHC game;

    public PlayerListener(SpeedUHC game) {
        this.game = game;
    }

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(ChatColor.GOLD + "Bienvenue sur le serveur Speed UHC");
        player.setScoreboard(this.game.scoreboard);
        
        switch (this.game.etat) {

            // L'UHC est en préparation
            case PREPARE:
                player.teleport(new Location(player.getWorld(), 10, 201, 10));
                break;

            // L'UHC est lancé
            case RUNNING:
                if (!this.game.joueurs.contains(player.getName())) {
                    player.sendMessage(ChatColor.RED + "L'UHC est lancé, veuillez attendre la prochaine session");
                    player.teleport(new Location(player.getWorld(), 0, 180, 0));
                    player.setGameMode(GameMode.SPECTATOR);
                }
                break;

        }
    }

    @EventHandler()
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler()
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(ChatColor.RED + ((Player) event.getEntity()).getName() + " est mort : " + event.getDeathMessage() + ". Alors on est nul Bernard?");
        this.game.joueursAlive.remove(((Player) event.getEntity()).getName());
        
        event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 2));
        
        // Vainqueur
        if(this.game.joueursAlive.size() == 1) {
            this.game.allPlayersMessage(this.game.joueursAlive.get(0) + " est le vainqueur de ce Speed UHC !", ChatColor.GREEN);
        }
    }
    
}
