package listeners;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import pluginminecraft.SpeedUHC;

/**
 *
 * @author Guillaume
 */
public class EntityListener implements Listener {

    private final SpeedUHC game;
    private Map<EntityType, Material> listeFood;

    public boolean invincible = true;

    public EntityListener(SpeedUHC game) {
        this.game = game;
        this.listeFood = new HashMap<>();

        // On ajoute les blocks à donner directement
        this.listeFood.put(EntityType.COW, Material.COOKED_BEEF);
        this.listeFood.put(EntityType.SHEEP, Material.COOKED_MUTTON);
        this.listeFood.put(EntityType.PIG, Material.COOKED_BEEF);
        this.listeFood.put(EntityType.CHICKEN, Material.COOKED_CHICKEN);
        this.listeFood.put(EntityType.FISHING_HOOK, Material.COOKED_FISH);
        this.listeFood.put(EntityType.RABBIT, Material.COOKED_RABBIT);

    }

    @EventHandler()
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player && invincible) {
            event.setCancelled(true);
        }
    }

    @EventHandler()
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Player)) {
            if (this.listeFood.containsKey(e.getType())) {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(this.listeFood.get(e.getType())));

                if (e.getType() == EntityType.COW) {
                    event.getDrops().add(new ItemStack(Material.LEATHER));
                }

                if (e.getType() == EntityType.CHICKEN) {
                    event.getDrops().add(new ItemStack(Material.FEATHER));
                }
            }
        }
    }
}
