package listeners;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import pluginminecraft.Etat;
import pluginminecraft.SpeedUHC;

/**
 *
 * @author Guillaume
 */
public class BlockListener implements Listener {

    private final SpeedUHC game;
    private Map<Material, Material> listeBlock;

    public BlockListener(SpeedUHC game) {
        this.game = game;
        this.listeBlock = new HashMap<>();

        // On ajoute les blocks à donner directement
        this.listeBlock.put(Material.IRON_ORE, Material.IRON_INGOT);
        this.listeBlock.put(Material.GOLD_ORE, Material.GOLD_INGOT);

    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {

        if (this.game.etat == Etat.RUNNING) {

            // On récupère le joueur et le matériel du block
            Player p = (Player) event.getPlayer();
            Material m = (Material) event.getBlock().getType();

            // On cherche dans la liste le block à donner au joueur
            if (this.listeBlock.containsKey(m)) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(this.listeBlock.get(m)));

                // On annule l'évènement du block et on le remplace par un block d'air
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
            }

        } else {
            event.setCancelled(true);
        }

    }

    @EventHandler()
    public void onBlockDamage(BlockDamageEvent event) {
        if(event.getItemInHand().getType() == Material.DIAMOND_PICKAXE && event.getBlock().getType() == Material.OBSIDIAN) {
            event.setInstaBreak(true);
        }
    }
    
}
